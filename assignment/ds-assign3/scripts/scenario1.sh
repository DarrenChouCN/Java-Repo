#!/usr/bin/env bash
set -euo pipefail
source scripts/common.sh

echo "[Scenario1] Starting 9 members (all reliable)"
launch_9 reliable reliable reliable reliable reliable reliable reliable reliable reliable

# Give servers time to bind ports and warm up
sleep 1.0

echo "[Scenario1] Propose from M1: 'M5'"
send_cmd 1 "propose M5"

# Wait a bit for consensus to form
sleep 3

print_consensus
print_key_phases

echo "[Scenario1] Stopping all members"
stop_all
