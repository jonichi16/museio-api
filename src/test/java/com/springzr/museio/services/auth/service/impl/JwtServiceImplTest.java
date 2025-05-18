package com.springzr.museio.services.auth.service.impl;

import com.springzr.museio.services.auth.model.Account;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class JwtServiceImplTest {

    private JwtServiceImpl jwtServiceImpl;
    private final Long accountId = 1L;

    @BeforeEach
    void setUp() {
        jwtServiceImpl = new JwtServiceImpl();
        // Test secret key
        jwtServiceImpl.secretKey = "r+uZcNlc4WPU5ilIFqkJTLVcEtPb3" +
                "hs7HY3gF0Ix4lo9zkr8RCgJ5c1UqQavbvfY" +
                "TfobHix3dK3mhIlh3ayHJg==";

        // 1 minute test token expiration
        jwtServiceImpl.tokenExpiration = 1000 * 60;
    }

    @Test
    void generateToken_shouldGenerateWithUser() {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        // when
        String token = jwtServiceImpl.generateToken(account.getId());

        // then
        assertThat(token).isNotNull();
        assertThat(jwtServiceImpl.extractId(token)).isEqualTo(accountId);
    }

    @Test
    void isTokenExpired_shouldCheckIfTokenIsExpired() {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        // when
        String token = jwtServiceImpl.generateToken(account.getId());
        assertThat(jwtServiceImpl.isTokenExpired(token)).isFalse();

        jwtServiceImpl.tokenExpiration = 0; // update expiration time
        String expiredToken = jwtServiceImpl.generateToken(account.getId());

        assertThat(jwtServiceImpl.isTokenExpired(expiredToken)).isTrue();
    }

    @Test
    void isTokenValid_shouldCheckIfTokenIsValid() {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        String token = jwtServiceImpl.generateToken(account.getId());

        assertThat(jwtServiceImpl.isTokenValid(token, account.getId())).isTrue();
    }

    @Test
    void isTokenValid_shouldCheckIfTokenIsInvalid() {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        Long invalidId = 123L;

        String token = jwtServiceImpl.generateToken(account.getId());

        assertThat(jwtServiceImpl.isTokenValid(token, invalidId)).isFalse();

        jwtServiceImpl.tokenExpiration = 0; // update expiration time
        String expiredToken = jwtServiceImpl.generateToken(invalidId);
        assertThat(jwtServiceImpl.isTokenValid(expiredToken, invalidId)).isFalse();
    }
}
