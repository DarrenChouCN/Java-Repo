#!/usr/bin/env bash
set -euo pipefail
source scripts/common.sh

# Profiles layout:
# - M2 = latent
# - M3 = failure
# - others = standard
echo "[Scenario3] Starting 9 members (mixed: latent + failure)"
launch_9 standard latent failure standard standard standard standard standard standard

sleep 1.2

# Test 3a: A standard member (M4) initiates a proposal.
echo "[Scenario3/3a] M4 proposes 'S3A'"
send_cmd 4 "propose S3A"
sleep 3
echo "[Scenario3/3a] Consensus so far:"
print_consensus

# Test 3b: A latent member (M2) initiates a proposal. Despite its high latency, the system should still reach consensus.
echo "[Scenario3/3b] M2 (latent) proposes 'S3B'"
send_cmd 2 "propose S3B"
sleep 4
echo "[Scenario3/3b] Consensus so far:"
print_consensus

# Test 3c: The failing member (M3) initiates a proposal, sends its PREPARE messages, and then "crashes" (terminates).
echo "[Scenario3/3c] M6 proposes 'S3C' (M3 will crash-once on its first PREPARE)"
send_cmd 6 "propose S3C"
sleep 4

print_consensus
print_key_phases

echo "[Scenario3] Stopping all members"
stop_all
