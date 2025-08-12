#!/bin/bash
# Run integration tests (Testcontainers)
mvn -DskipTests=false -Dtest=*IT test
