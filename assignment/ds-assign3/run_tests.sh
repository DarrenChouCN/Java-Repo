#!/usr/bin/env bash
set -euo pipefail

# Run all scenarios with a fresh timestamped log dir
export TS="$(date +%Y%m%d-%H%M%S)"
mkdir -p logs/$TS

echo "===> Running Scenario 1 (all reliable, single proposal)"
bash scripts/scenario1.sh

echo
echo "===> Running Scenario 2 (concurrent proposals: M1 vs M8)"
bash scripts/scenario2.sh

echo
echo "===> Running Scenario 3 (latent + failure mixed)"
bash scripts/scenario3.sh

echo
echo "===> All scenarios finished. Logs at: logs/$TS"
echo "You can grep results, e.g.:"
echo "  grep -R \"^CONSENSUS\" logs/$TS | sort"
