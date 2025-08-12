# monolith-registry

Service registry module (lightweight Nacos-like).

Features:
- Register service instance
- Heartbeat endpoint
- Discover endpoint with local Caffeine cache (TTL 10 minutes)
- Scheduled cleaner to mark stale instances DOWN

Package: com.hugh.base.service.registry
