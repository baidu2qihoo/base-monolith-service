#!/bin/bash
# Run gatling simulation (requires gatling plugin or using mvn test with gatling dependency)
# This example runs the Scala script using Gatling CLI if installed
if command -v gatling.sh >/dev/null 2>&1; then
  gatling.sh -s BasicSimulation
else
  echo "Gatling CLI not found. You can run via Maven plugin or install Gatling."
fi
