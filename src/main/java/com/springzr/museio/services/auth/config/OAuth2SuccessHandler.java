package com.springzr.museio.services.auth.config;

import com.springzr.museio.services.auth.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Handles successful OAuth2 login by issuing a JWT and setting it as an HttpOnly cookie.
 *
 * <p>After authentication, a secure cookie containing the access token is set, and the user is
 * redirected to a configured URI.</p>
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.jwt.auth-redirect-uri}")
    String authRedirectUri;
    private final JwtService jwtService;
    private final TokenStore tokenStore;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String accountId = oauth2User.getName();

        String token = jwtService.generateToken(accountId);

        String state = UUID.randomUUID().toString();
        tokenStore.store(state, token);

        response.sendRedirect(authRedirectUri + state);
    }
}
