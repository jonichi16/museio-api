package com.springzr.museio.services.auth.service;

import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;

public interface AuthService {

    TokenResponse getToken(TokenRequest request);

}
