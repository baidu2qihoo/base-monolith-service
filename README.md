# Migrated base-monolith-service (no git history)

This project was migrated automatically from the uploaded repository.
Modules created: monolith-core-api, monolith-core-impl, monolith-config, monolith-registry, monolith-gateway, monolith-stability, monolith-tracing, monolith-metrics, monolith-app, monolith-test

## How to build locally
- Ensure Java 17 and Maven are installed.
- Start a local MySQL and Redis or adjust `application.yml` in modules.
- From project root run: `mvn -T 1C -DskipTests package`

## Notes
- Dependency merging was best-effort from original module poms. Please review each `pom.xml` in modules for version alignment and remove duplicates/conflicts.
- This migration does NOT preserve Git history (per your instruction).
