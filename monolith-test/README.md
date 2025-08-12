# monolith-test

Full testing module including:
- Integration tests with Testcontainers (MySQL, Redis)
- WireMock-based downstream mocks
- Gatling load test script (Scala)
- JMH microbenchmarks
- Scripts to run integration tests, Gatling and JMH

Usage:
- mvn -DskipTests=false -Dtest=*IT test   # run integration tests
- ./scripts/run-gatling.sh               # run gatling (requires gatling CLI)
- ./scripts/run-jmh.sh                   # run jmh (built fat jar via shade)
