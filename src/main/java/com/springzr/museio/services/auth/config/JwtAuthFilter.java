package com.springzr.museio.services.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.dto.ErrorResponse;
import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.auth.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> tokenOpt = extractTokenFromAuthorizationHeader(request);

        if (
                tokenOpt.isPresent()
                        && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            String token = tokenOpt.get();
            try {
                Long accountId = jwtService.extractId(token);
                if (jwtService.isTokenValid(token, accountId)) {
                    authenticateUser(request, accountId);
                }
            } catch (JwtException ex) {
                LOGGER.warn("Invalid JWT token: {}", ex.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                MSResponse<Void> errorResponse = ErrorResponse.<Void>builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message("Unauthorized")
                        .errorCode(ErrorCode.UNAUTHORIZED)
                        .build();

                String json = objectMapper.writeValueAsString(errorResponse);

                response.getWriter().write(json);
                return;
            } catch (Exception ex) {
                LOGGER.error("Unexpected error while processing JWT token", ex);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return Optional.of(header.substring(BEARER_PREFIX.length()).trim());
        }
        return Optional.empty();
    }

    private void authenticateUser(HttpServletRequest request, Long accountId) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                accountId, null, null
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
