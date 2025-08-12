# monolith-stability

Service stability module (Sentinel-like behavior).

Features:
- Loads rules from `rule_store` table (RuleEntity)
- Applies FLOW and DEGRADE rules into in-memory token buckets and degrade flags
- Listens to ConfigChangeEvent to reload rules for a service
- REST admin endpoints to reload rules and try acquire a token

Notes:
- This is a simplified local implementation for demonstration and testing.
- For production, integrate Alibaba Sentinel Java SDK and use its APIs to register dynamic rules,
  or use Redis/Sentinel for distributed rate limiting.
