# monolith-metrics

Prometheus metrics module using Micrometer.

Features:
- Exposes /actuator/prometheus (via Spring Boot Actuator and micrometer-registry-prometheus)
- Adds common tags: env, tenant, service
- Demo endpoints to generate metrics (ping, work, enqueue/dequeue)
- Intended to be run alongside other modules or standalone for metrics testing.

Package: com.hugh.base.service.metrics
