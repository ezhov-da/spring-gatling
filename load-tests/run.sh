#!/usr/bin/env bash

set -e

gradle :gatlingRun

if [[ -d "build/reports/gatling" ]]; then
  cp -r build/reports/gatling /report
fi