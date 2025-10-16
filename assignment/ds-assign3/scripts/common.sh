#!/usr/bin/env bash
set -euo pipefail

# ------- configurable env -------
: "${JAVA_BIN:=java}"
: "${MAIN_CLASS:=au.edu.adelaide.paxos.app.CouncilMemberMain}"
: "${CLASSPATH:=target/classes:target/paxos.jar}"
: "${CONFIG_FILE:=network.config}"

# --------------------------------------------------------------------
: "${TS:?must set TS (timestamp) in run_tests.sh}"
LOG_DIR="logs/$TS"
PIPE_DIR="pipes/$TS"
PID_DIR="pids/$TS"

mkdir -p "$LOG_DIR" "$PIPE_DIR" "$PID_DIR"

# Create/replace FIFO and start one member
start_member() {
  local id="$1" profile="$2"
  local pipe="$PIPE_DIR/M${id}.in"

  # remove existing fifo/file then create a fresh one
  if [[ -p "$pipe" || -e "$pipe" ]]; then
    rm -f "$pipe"
  fi
  mkfifo -m 600 "$pipe"

  (
    set -o pipefail
    "$JAVA_BIN" -cp "$CLASSPATH" "$MAIN_CLASS" \
      --id "$id" --config "$CONFIG_FILE" --profile "$profile" \
      < "$pipe" > "$LOG_DIR/M${id}.log" 2>&1
  ) &
  echo $! > "$PID_DIR/M${id}.pid"
  sleep 0.15
}

# Send a line to member's stdin
send_cmd() {
  local id="$1" cmd="$2"
  local pipe="$PIPE_DIR/M${id}.in"
  if [[ -p "$pipe" ]]; then
    printf "%s\n" "$cmd" > "$pipe"
  else
    echo "WARN: pipe not found for M$id ($pipe)" >&2
  fi
}

# Stop all members (SIGTERM then SIGKILL), and clean FIFOs/PIDs
stop_all() {
  for f in "$PID_DIR"/M*.pid; do
    [[ -f "$f" ]] || continue
    local pid
    pid="$(cat "$f" 2>/dev/null || true)"
    if [[ -n "${pid:-}" ]]; then
      kill "$pid" 2>/dev/null || true
    fi
  done

  sleep 0.5

  for f in "$PID_DIR"/M*.pid; do
    [[ -f "$f" ]] || continue
    local pid
    pid="$(cat "$f" 2>/dev/null || true)"
    if [[ -n "${pid:-}" ]] && kill -0 "$pid" 2>/dev/null; then
      kill -9 "$pid" 2>/dev/null || true
    fi
  done

  # cleanup so the next scenario can recreate fresh FIFOs/PIDs
  rm -f "$PIPE_DIR"/M*.in  2>/dev/null || true
  rm -f "$PID_DIR"/M*.pid  2>/dev/null || true
}

# Launch a 9-member cluster with per-member profiles (args: p1..p9)
launch_9() {
  local p1="$1" p2="$2" p3="$3" p4="$4" p5="$5" p6="$6" p7="$7" p8="$8" p9="$9"
  start_member 1 "$p1"
  start_member 2 "$p2"
  start_member 3 "$p3"
  start_member 4 "$p4"
  start_member 5 "$p5"
  start_member 6 "$p6"
  start_member 7 "$p7"
  start_member 8 "$p8"
  start_member 9 "$p9"
}

# Collate & print consensus lines
print_consensus() {
  echo "---- CONSENSUS LINES ----"
  grep -hR "^CONSENSUS" "$LOG_DIR" | sort || true
}

# Print key phases quickly (TX/RX/CONSENSUS)
print_key_phases() {
  echo "---- KEY PHASES (TX/RX/CONSENSUS) ----"
  grep -hR -E "^(TX|RX|CONSENSUS)" "$LOG_DIR" | sort || true
}
