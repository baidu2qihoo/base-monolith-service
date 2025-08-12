package com.hugh.base.service.gateway.filter;

import com.hugh.base.service.trace.TraceIdUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Simple filter to ensure traceId present and validate (mock) JWT.
 */
@Component
public class TraceAndJwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
        try {
            String incomingTrace = req.getHeader("X-Trace-Id");
            if (incomingTrace != null && !incomingTrace.isEmpty()) {
                TraceIdUtils.set(incomingTrace);
            } else {
                TraceIdUtils.getOrCreate();
            }
            // Mock JWT check: in production, verify JWT signature and tenant claims
            String auth = req.getHeader("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                // allow anonymous for some endpoints
            } else {
                String token = auth.substring(7);
                // TODO: validate token and set security context
                MDC.put("user", "jwtUser");
            }
            chain.doFilter(req, resp);
        } finally {
            TraceIdUtils.clear();
            MDC.remove("user");
        }
    }
}
