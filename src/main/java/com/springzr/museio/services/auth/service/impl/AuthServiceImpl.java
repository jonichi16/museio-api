package com.springzr.museio.services.auth.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.config.TokenStore;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class that implements {@link AuthService}.
 *
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenStore tokenStore;

    @Override
    public TokenResponse getToken(TokenRequest request) {

        String id = request.id();
        if (id == null) {
            throw new MSException("id is required", HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
        }

        String token = tokenStore.consume(id);
        if (token == null) {
            throw new MSException("Unauthorized", HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED);
        }
        String bearerType = "Bearer";

        return TokenResponse.builder()
                .accessToken(token)
                .bearerType(bearerType)
                .build();
    }
}

