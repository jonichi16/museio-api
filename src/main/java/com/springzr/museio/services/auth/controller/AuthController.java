package com.springzr.museio.services.auth.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.RegisterResponse;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller responsible for handling user authentication-related endpoints.
 *
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    /**
     * Retrieve token based on id from auth callback.
     *
     * @param request containing the token id
     * @return a {@link ResponseEntity} containing {@link MSResponse} with the jwt access token
     *     and bearer type
     */
    @PostMapping("/token")
    public ResponseEntity<MSResponse<TokenResponse>> getToken(
            @RequestBody @Valid TokenRequest request
    ) {
        LOGGER.info("Request: id={}", request.id());

        TokenResponse token = authService.getToken(request);
        HttpStatus status = HttpStatus.OK;
        MSResponse<TokenResponse> response = SuccessResponse.<TokenResponse>builder()
                .code(status.value())
                .message("Authentication successful")
                .data(token)
                .build();

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Register a new user to the system.
     *
     * @param username the user's username
     * @param bio the user's bio
     * @param profilePicture the profile picture of the user
     * @return a {@link ResponseEntity} containing {@link MSResponse} with the username
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MSResponse<RegisterResponse>> register(
            @RequestParam("username") String username,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        RegisterResponse registerResponse = authService.register(username, bio, profilePicture);

        HttpStatus status = HttpStatus.CREATED;
        MSResponse<RegisterResponse> response = SuccessResponse.<RegisterResponse>builder()
                .code(status.value())
                .message("Profile created successfully")
                .data(registerResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
