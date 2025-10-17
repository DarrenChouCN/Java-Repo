#!/usr/bin/env bash
# 最小可跑：不使用 -e/pipefail，避免早退
set -u

ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")"/.. && pwd)"
cd "$ROOT"

: "${CONFIG_FILE:=network.config}"
: "${N_MEMBERS:=9}"
: "${READY_TIMEOUT:=20}"
: "${MAIN_CLASS:=au.edu.adelaide.paxos.app.CouncilMemberMain}"
: "${CLASSPATH:=target/classes}"

LOG_DIR="${LOG_DIR:-logs}"
PID_DIR="${PID_DIR:-pids}"
PIPE_DIR="${PIPE_DIR:-pipes}"
mkdir -p "$LOG_DIR" "$PID_DIR" "$PIPE_DIR"

# --- config 读取（三列：id host port；允许 CRLF）
get_host() {
  awk -v id="$1" 'BEGIN{h=""} $1==id{h=$2; exit} END{print h}' "$CONFIG_FILE" | tr -d '\r'
}
get_port() {
  awk -v id="$1" 'BEGIN{p=""} $1==id{p=$3; exit} END{print p}' "$CONFIG_FILE" | tr -d '\r'
}

# --- 端口监听检测（用 netstat，避免 PowerShell 转义坑）
port_ready() {
  local port="$1"
  if [[ -z "$port" ]]; then return 1; fi
  netstat -ano 2>/dev/null | tr -d '\r' \
    | grep -E "LISTEN|LISTENING" | grep -q ":${port}\b"
}

# --- 启动一个成员，把 stdin 绑到命名管道（给 CLI 用）
start_member() {
  local id="$1" profile="${2:-reliable}"
  local logf="$LOG_DIR/member-$id.log"
  local pf="$PIPE_DIR/m$id"
  [[ -p "$pf" ]] || mkfifo "$pf"
  nohup java -cp "$CLASSPATH" "$MAIN_CLASS" \
    --id "$id" --profile "$profile" --config "$CONFIG_FILE" \
    < "$pf" > "$logf" 2>&1 &
  echo $! > "$PID_DIR/member-$id.pid"
  echo "[start] M$id profile=$profile (pid=$(cat "$PID_DIR/member-$id.pid"))"
}

send_cmd() {
  local id="$1"; shift
  local pf="$PIPE_DIR/m$id"
  [[ -p "$pf" ]] || { echo "[ERR] pipe not found: $pf" >&2; return 1; }
  printf "%s\n" "$*" > "$pf"
}

wait_members_up() {
  local n="${1:-$N_MEMBERS}" timeout="${2:-$READY_TIMEOUT}"
  local deadline=$((SECONDS+timeout))
  echo "[wait] members up (n=$n, timeout=${timeout}s)"

  while (( SECONDS < deadline )); do
    local ok=0
    for i in $(seq 1 "$n"); do
      local port; port="$(get_port "$i")"
      if port_ready "$port"; then ((ok++)); fi
    done
    if (( ok >= (n/2+1) )); then
      echo "[wait] majority ready: $ok/$n"
      return 0
    fi
    sleep 0.3
  done

  echo "[ERROR] members not ready in time" >&2
  return 1
}

stop_all() {
  shopt -s nullglob
  if command -v taskkill >/dev/null 2>&1; then
    for f in "$PID_DIR"/member-*.pid; do
      pid="$(cat "$f" 2>/dev/null || true)"
      [[ -n "${pid:-}" ]] && taskkill //F //PID "$pid" >/dev/null 2>&1 || true
      rm -f "$f"
    done
  else
    for f in "$PID_DIR"/member-*.pid; do
      pid="$(cat "$f" 2>/dev/null || true)"
      [[ -n "${pid:-}" ]] && kill -9 "$pid" >/devnull 2>&1 || true
      rm -f "$f"
    done
  fi
}
