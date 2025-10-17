#!/usr/bin/env bash
set -u
source "$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)/common.sh"

echo "===> Scenario 2 (concurrent proposals)"

for i in $(seq 1 "$N_MEMBERS"); do
  start_member "$i" standard
done

if ! wait_members_up "$N_MEMBERS" "$READY_TIMEOUT"; then
  echo "---- tail logs (M1..M3) ----"
  for i in 1 2 3; do
    echo "== logs/member-$i.log =="; sed -n '1,120p' "logs/member-$i.log" || true
  done
  stop_all
  exit 1
fi

echo "[Scenario2] concurrent proposals: M1->M5, M8->M7"
send_cmd 1 "propose M5"
sleep 0.05
send_cmd 8 "propose M7"

sleep 3
stop_all
echo "[Scenario2] Done."
