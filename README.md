

## Updates in updated package
- Unified versions and parent POM (1.0.0)
- Added GitHub Actions CI workflow (.github/workflows/ci.yml)
- Integrated Atomix dependency and sample starter in monolith-core
- Added Spring Security and RSA JWT skeleton in monolith-core


FINAL UPDATES:
- Atomix demo runnable class: com.hugh.base.service.raft.atomix.AtomixDemoMain
- JwtAuthenticationFilter implemented and wired; set JWT_PUBLIC_KEY_B64 env to enable
- CI extended: integration-tests job and docker-build-and-push job (requires secrets)
- cluster-startup.sh updated to start Atomix demo nodes and monolith-app
