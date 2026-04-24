#!/usr/bin/env bash
set -e

CPUS=(0.5 1.0 1.5 2.0 2.5 3.0)
PROFILES=(5 50 95)
VUS=40
DURATION=2m
BASE_URL=http://localhost:8080
ORIGIN=${1:-local}

mkdir -p results

for cpu in "${CPUS[@]}"; do
  export APP_CPUS="$cpu"
  docker compose up -d --force-recreate app
  sleep 15

  for write in "${PROFILES[@]}"; do
    read=$((100 - write))
    k6 run \
      -e BASE_URL="$BASE_URL" \
      -e VUS="$VUS" \
      -e DURATION="$DURATION" \
      -e WRITE_SHARE="$write" \
      --out json="results/${ORIGIN}_cpu${cpu}_w${write}_r${read}.json" \
      load-test6.js
  done
done