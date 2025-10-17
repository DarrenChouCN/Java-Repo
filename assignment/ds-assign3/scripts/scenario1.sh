#!/usr/bin/env bash
set -u
source "$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)/common.sh"

echo "===> Scenario 1 (all reliable, single proposal)"

for i in $(seq 1 "$N_MEMBERS"); do
  start_member "$i" reliable
done

if ! wait_members_up "$N_MEMBERS" "$READY_TIMEOUT"; then
  echo "---- tail logs (M1..M3) ----"
  for i in 1 2 3; do
    echo "== member-$i.log =="
    sed -n '1,120p' "logs/member-$i.log" || true
  done
  stop_all
  exit 1
fi

echo "[Scenario1] M1: propose M5"
send_cmd 1 "propose M5"

sleep 2
stop_all
echo "[Scenario1] Done."
