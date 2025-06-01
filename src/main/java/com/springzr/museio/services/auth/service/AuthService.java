package com.springzr.museio.services.auth.service;

import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.RegisterResponse;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Register the user based on the input.
     *
     * @param username the user's username
     * @param bio the user's bio
     * @param profilePicture the user's profile picture
     * @return {@link RegisterResponse} that contains the username of the user
     */
    RegisterResponse register(String username, String bio, MultipartFile profilePicture);

}
