package com.springzr.museio.services.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.config.MSOAuth2UserService;
import com.springzr.museio.services.auth.config.OAuth2SuccessHandler;
import com.springzr.museio.services.auth.config.SecurityConfig;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.auth.repository.AccountRepository;
import com.springzr.museio.services.auth.service.AuthService;
import com.springzr.museio.services.auth.service.JwtService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private MSOAuth2UserService msOAuth2UserService;
    @MockBean
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @MockBean
    private AuthService authService;

    @Test
    public void getToken_shouldReturn200Ok() throws Exception {
        // given
        String id = "81470020-4855-47b9-b8f4-259b337e97b0";
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

        // then
        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("tokenSuccess",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void getToken_shouldReturn400BadRequest() throws Exception {
        // given
        String invalidRequest = "{ }";

        // when

        // then
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(
                        document("tokenBadRequest",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    public void getToken_shouldReturn401Unauthorized() throws Exception {
        // given
        String id = "81470020-4855-47b9-b8f4-259b337e97b0";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        // when
        when(authService.getToken(tokenRequest))
                .thenThrow(
                        new MSException(
                                "Unauthorized",
                                HttpStatus.UNAUTHORIZED,
                                ErrorCode.UNAUTHORIZED
                        )
                );

        // then
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(
                        document("tokenUnauthorized",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }
}
