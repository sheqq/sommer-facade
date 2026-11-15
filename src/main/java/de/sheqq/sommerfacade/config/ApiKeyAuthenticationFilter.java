package de.sheqq.sommerfacade.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final String HEADER_NAME = "X-API-Key";
    private final byte[] expectedApiKey;

    public ApiKeyAuthenticationFilter(String apiKey) {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException("API_KEY must be configured");
        }
        this.expectedApiKey = apiKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String providedApiKey = request.getHeader(HEADER_NAME);
        if (providedApiKey == null || !MessageDigest.isEqual(
                expectedApiKey, providedApiKey.getBytes(StandardCharsets.UTF_8))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("api-key", null, AuthorityUtils.NO_AUTHORITIES));
        filterChain.doFilter(request, response);
    }
}
