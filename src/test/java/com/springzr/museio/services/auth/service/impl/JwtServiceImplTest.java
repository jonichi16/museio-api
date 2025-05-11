package com.springzr.museio.services.auth.service.impl;

import com.springzr.museio.services.auth.model.Account;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class JwtServiceImplTest {

    private JwtServiceImpl jwtServiceImpl;
    private final UUID accountId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

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
    public void generateToken_shouldGenerateWithUser() throws Exception {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        // when
        String token = jwtServiceImpl.generateToken(account.getId().toString());

        // then
        assertThat(token).isNotNull();
        assertThat(jwtServiceImpl.extractId(token)).isEqualTo(accountId);
    }

    @Test
    public void isTokenExpired_shouldCheckIfTokenIsExpired() throws Exception {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        // when
        String token = jwtServiceImpl.generateToken(account.getId().toString());
        assertThat(jwtServiceImpl.isTokenExpired(token)).isFalse();

        jwtServiceImpl.tokenExpiration = 0; // update expiration time
        String expiredToken = jwtServiceImpl.generateToken(account.getId().toString());

        assertThat(jwtServiceImpl.isTokenExpired(expiredToken)).isTrue();
    }

    @Test
    public void isTokenValid_shouldCheckIfTokenIsValid() throws Exception {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        String token = jwtServiceImpl.generateToken(account.getId().toString());

        assertThat(jwtServiceImpl.isTokenValid(token, account.getId().toString())).isTrue();
    }

    @Test
    public void isTokenValid_shouldCheckIfTokenIsInvalid() throws Exception {
        // given
        Account account = Account.builder()
                .id(accountId)
                .email("john.doe@mail.com")
                .name("John Doe")
                .build();

        String invalidId = "123e4567-e89b-12d3-a456-426614174111";

        String token = jwtServiceImpl.generateToken(account.getId().toString());

        assertThat(jwtServiceImpl.isTokenValid(token, invalidId)).isFalse();

        jwtServiceImpl.tokenExpiration = 0; // update expiration time
        String expiredToken = jwtServiceImpl.generateToken(invalidId);
        assertThat(jwtServiceImpl.isTokenValid(expiredToken, invalidId)).isFalse();
    }
}
