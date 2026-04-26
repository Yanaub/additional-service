#!/usr/bin/env bash
set -e

CPUS=(0.5 1.0 1.5 2.0)
PROFILES=(5 50 95)
VUS=50
DURATION=1.5m


TARGET=${1:-local}

case "$TARGET" in
  local)
    BASE_URL=http://10.60.3.4:8080
    SSH_CMD="ssh hl8"

    ;;
  hlv)
    BASE_URL=http://localhost:8080
    SSH_CMD="ssh hlv"
    ;;
  *)
    echo "Unknown target: $TARGET"
    exit 1
    ;;
esac

mkdir -p results

for cpu in "${CPUS[@]}"; do

  $SSH_CMD bash -c "
    set -e
    export APP_CPU_LIMIT=$cpu
    cd ~/module1
    docker compose up -d --force-recreate app
    "
  sleep 60

  for write in "${PROFILES[@]}"; do
    read=$((100 - write))
    k6 run \
      -e BASE_URL="$BASE_URL" \
      -e VUS="$VUS" \
      -e DURATION="$DURATION" \
      -e WRITE_SHARE="$write" \
      --out json="results/${TARGET}_cpu${cpu}_w${write}_r${read}.json" \
      load-test6.js
  done
done