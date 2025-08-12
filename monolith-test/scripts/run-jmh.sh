#!/bin/bash
# Build shaded JMH jar then run
mvn -q -DskipTests package
java -jar target/monolith-test-0.0.1-SNAPSHOT.jar
