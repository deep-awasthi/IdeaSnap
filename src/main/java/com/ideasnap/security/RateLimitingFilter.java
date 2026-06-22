package com.ideasnap.security;

import com.ideasnap.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RateLimitingFilter.class);

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        
        // Exclude Swagger & OpenAPI paths from rate limiting
        if (path.startsWith("/api-docs") || path.startsWith("/swagger-ui") || path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = resolveClientKey(request);

        if (!rateLimitingService.isAllowed(key)) {
            log.warn("Rate limit exceeded for client: {}", key);
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            String body = String.format(
                    "{\"timestamp\":\"%s\",\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Please try again later.\",\"path\":\"%s\"}",
                    Instant.now().toString(),
                    request.getRequestURI()
            );
            response.getWriter().write(body);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveClientKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal().toString())) {
            return auth.getName();
        }

        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
