package com.springzr.museio.services.auth.service;

import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;

/**
 * Service interface for authentication-related operations.
 *
 */
public interface AuthService {

    /**
     * Retrieve token based on id.
     *
     * @param request contains token id
     * @return {@link TokenResponse} that contains jwt access token and bearer type
     */
    TokenResponse getToken(TokenRequest request);

}
