package com.springzr.museio.services.auth.config;

import com.springzr.museio.services.auth.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter that checks for a JWT token in the HTTP request's Authorization header.
 * If a valid token is found, it authenticates the user by setting the authentication
 * details in the Spring Security context.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);
    private static final String ACCESS_TOKEN_COOKIE = "__accessToken";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> tokenOpt = extractTokenFromCookies(request);

        if (
                tokenOpt.isPresent()
                        && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            String token = tokenOpt.get();
            try {
                UUID accountId = jwtService.extractId(token);
                if (jwtService.isTokenValid(token, accountId.toString())) {
                    authenticateUser(request, accountId);
                }
            } catch (JwtException ex) {
                LOGGER.warn("Invalid JWT token: {}", ex.getMessage());
            } catch (Exception ex) {
                LOGGER.error("Unexpected error while processing JWT token", ex);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        for (Cookie cookie : request.getCookies()) {
            if (ACCESS_TOKEN_COOKIE.equals(cookie.getName())) {
                return Optional.ofNullable(cookie.getValue());
            }
        }

        return Optional.empty();
    }

    private void authenticateUser(HttpServletRequest request, UUID accountId) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                accountId, null, null
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
