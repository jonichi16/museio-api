package com.springzr.museio.services.auth.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.config.TokenStore;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthServiceImplTest {

    @Mock
    private TokenStore tokenStore;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void getToken_shouldReturnTokenResponse() {
        // given
        String id = "sampleId";
        String accessToken = "accessToken";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        // when
        when(tokenStore.consume(id)).thenReturn(accessToken);
        TokenResponse token = authServiceImpl.getToken(tokenRequest);

        // then
        verify(tokenStore, times(1)).consume(id);
        assertThat(token.accessToken()).isEqualTo(accessToken);
        assertThat(token.bearerType()).isEqualTo("Bearer");
    }

    @Test
    void getToken_whenIdIsNull_shouldThrow400BadRequest() {
        // given
        TokenRequest tokenRequest = TokenRequest.builder()
                .build();

        // when
        // then
        assertThatThrownBy(() -> authServiceImpl.getToken(tokenRequest))
                .isInstanceOf(MSException.class)
                .hasMessage("id is required")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BAD_REQUEST);
        verify(tokenStore, never()).consume(null);
    }

    @Test
    void getToken_whenTokenStoreReturnNull_shouldThrow400Unauthorized() {
        // given
        String id = "sampleId";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        // when
        when(tokenStore.consume(id)).thenReturn(null);

        // then
        assertThatThrownBy(() -> authServiceImpl.getToken(tokenRequest))
                .isInstanceOf(MSException.class)
                .hasMessage("Unauthorized")
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNAUTHORIZED)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.UNAUTHORIZED);
    }

}
