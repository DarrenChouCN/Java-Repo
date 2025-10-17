#!/usr/bin/env bash
set -u
source "$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)/common.sh"

SETTLE="${SETTLE:-4}"

echo "===> Scenario 3 (mixed profiles: latent & failure present)"

profile_for() {
  case "$1" in
    2) echo "latent" ;;
    3) echo "failure" ;;
    *) echo "standard" ;;
  esac
}

for i in $(seq 1 "$N_MEMBERS"); do
  start_member "$i" "$(profile_for "$i")"
done

if ! wait_members_up "$N_MEMBERS" "$READY_TIMEOUT"; then
  echo "---- tail logs (M1..M3) ----"
  for i in 1 2 3; do
    echo "== logs/member-$i.log =="; sed -n '1,160p' "logs/member-$i.log" || true
  done
  stop_all
  exit 1
fi

echo "[Scenario3/3a] M4 proposes 'S3A'"
send_cmd 4 "propose S3A"
sleep "$SETTLE"

echo "[Scenario3/3b] M2 (latent) proposes 'S3B'"
send_cmd 2 "propose S3B"
sleep "$SETTLE"

echo "[Scenario3/3c] M6 proposes 'S3C'"
send_cmd 6 "propose S3C"
sleep "$SETTLE"

stop_all
echo "[Scenario3] Done."
