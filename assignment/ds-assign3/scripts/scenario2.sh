#!/usr/bin/env bash
set -euo pipefail
source scripts/common.sh

echo "[Scenario2] Starting 9 members (standard network)"
launch_9 standard standard standard standard standard standard standard standard standard

sleep 1.0

echo "[Scenario2] Concurrent proposals: M1='A', M8='B'"
send_cmd 1 "propose A"
# tiny offset to emulate near-simultaneous
sleep 0.05
send_cmd 8 "propose B"

sleep 4

print_consensus

# Optional: quick check whether only one distinct value appears
distinct=$(grep -hR "^CONSENSUS" "$LOG_DIR" | awk '{for(i=1;i<=NF;i++) if($i ~ /^value=/){print $i}}' | sort -u | wc -l | tr -d ' ')
echo "[Scenario2] Distinct consensus values: $distinct (expect 1)"
print_key_phases

echo "[Scenario2] Stopping all members"
stop_all
