package com.springzr.museio.libs.common.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TokenUtilTest {

    @Mock
    Authentication auth;

    @BeforeEach
    void setUp() {
        when(auth.getPrincipal()).thenReturn(1L);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAccountId_shouldReturnAccountId() {
        // given

        // when
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(1L);
        Long accountId = TokenUtil.getAccountId();

        // then
        assertThat(accountId).isEqualTo(1L);
    }
}
