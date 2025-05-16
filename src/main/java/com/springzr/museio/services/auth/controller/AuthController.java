package com.springzr.museio.services.auth.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<MSResponse<TokenResponse>> getToken(@RequestBody @Valid TokenRequest request) {

        TokenResponse token = authService.getToken(request);
        HttpStatus status = HttpStatus.OK;
        MSResponse<TokenResponse> response = SuccessResponse.<TokenResponse>builder()
                .code(status.value())
                .message("Authentication successful")
                .data(token)
                .build();

        return ResponseEntity.status(status).body(response);
    }

}
