package com.hugh.base.service.trace;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter to ensure trace id propagation.
 */
public class TraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String incoming = req.getHeader("X-Trace-Id");
            if (incoming != null && !incoming.isBlank()) {
                TraceIdUtils.set(incoming);
            } else {
                TraceIdUtils.getOrCreate();
            }
            chain.doFilter(request, response);
        } finally {
            TraceIdUtils.clear();
        }
    }
}
