package com.springzr.museio.services.auth.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.auth.service.AuthService;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @Test
    void getToken_shouldCallAuthServiceOnce() {
        // given
        String id = "sampleId";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        // when
        authController.getToken(tokenRequest);

        // then
        verify(authService, times(1)).getToken(tokenRequest);
    }
    
    @Test
    void getToken_shouldReturnCorrectResponseBody() {
        // given
        String id = "sampleId";
        String accessToken = "accessToken";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(accessToken)
                .bearerType("Bearer")
                .build();
        
        // when
        when(authService.getToken(tokenRequest)).thenReturn(tokenResponse);
        ResponseEntity<MSResponse<TokenResponse>> response = authController.getToken(tokenRequest);
        
        // then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getData().accessToken()).isEqualTo(accessToken);
        assertThat(Objects.requireNonNull(response.getBody()).getData().bearerType()).isEqualTo("Bearer");
        assertThat(response.getBody().getMessage()).isEqualTo("Authentication successful");
        assertThat(response.getBody().getCode()).isEqualTo(200);
    }

}
