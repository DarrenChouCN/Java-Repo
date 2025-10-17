#!/usr/bin/env bash
set -Eeuo pipefail
trap 'echo "[ERROR] line $LINENO failed: $BASH_COMMAND (exit=$?)" >&2' ERR

cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

echo "[Runner] Build check (you can skip if already built): mvn -q -DskipTests package"
mvn -q -DskipTests package

echo "[Runner] Running Scenario 1 only"
bash ./scripts/scenario1.sh
echo "[Runner] Done."
