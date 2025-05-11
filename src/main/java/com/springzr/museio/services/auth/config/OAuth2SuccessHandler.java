package com.springzr.museio.services.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.auth.model.dto.AuthTokenResponse;
import com.springzr.museio.services.auth.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String accountId = oAuth2User.getName();

        String token = jwtService.generateToken(accountId);

        AuthTokenResponse authTokenResponse = AuthTokenResponse.builder()
                .accountId(accountId)
                .accessToken(token)
                .build();

        HttpStatus status = HttpStatus.OK;
        MSResponse<AuthTokenResponse> responseBody = SuccessResponse.<AuthTokenResponse>builder()
                .code(status.value())
                .message("Authentication successful")
                .data(authTokenResponse)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
