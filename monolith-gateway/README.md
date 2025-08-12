# monolith-gateway

Lightweight API gateway module.

Features:
- Global filter to ensure traceId and perform placeholder JWT handling
- Dynamic, in-memory route definitions (RouteService)
- Simple synchronous proxy controller forwarding requests to backend services discovered via Registry
- Local token-bucket rate limiter per tenant+route (in-memory, not distributed)
- Admin endpoints to upsert/remove/list routes

Package: com.hugh.base.service.gateway
