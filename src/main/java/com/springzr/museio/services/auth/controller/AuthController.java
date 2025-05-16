package com.springzr.museio.services.auth.controller;

import com.springzr.museio.services.auth.config.TokenStore;
import java.util.Map;
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

    private final TokenStore tokenStore;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody Map<String, String> body) {
        String state = body.get("state");
        if (state == null) return ResponseEntity.badRequest().build();

        String token = tokenStore.consume(state);
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(Map.of("accessToken", token, "bearerType", "Bearer"));
    }

}
