// ... existing code ...
// 新增 JWT 认证过滤器
package com.hugh.base.service.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        // 检查 JWT token
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 验证 token 有效性
            if (isValidToken(token)) {
                // 将用户信息添加到请求头中
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-ID", extractUserIdFromToken(token))
                        .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }
        }

        // 如果认证失败，返回 401
        // 这里简化处理，实际应该构建完整的错误响应
        return exchange.getResponse().setComplete();
    }

    private boolean isValidToken(String token) {
        // 实现 JWT token 验证逻辑
        return true; // 简化示例
    }

    private String extractUserIdFromToken(String token) {
        // 从 token 中提取用户 ID
        return "user123"; // 简化示例
    }
}
// ... existing code ...
