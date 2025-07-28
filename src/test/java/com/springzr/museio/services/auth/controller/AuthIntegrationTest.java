package com.springzr.museio.services.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.config.MSOAuth2UserService;
import com.springzr.museio.services.auth.config.OAuth2SuccessHandler;
import com.springzr.museio.services.auth.config.SecurityConfig;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.RegisterResponse;
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
import org.springframework.mock.web.MockMultipartFile;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class AuthIntegrationTest {

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
    void getToken_shouldReturn200Ok() throws Exception {
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
    void getToken_shouldReturn400BadRequest() throws Exception {
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
    void getToken_shouldReturn401Unauthorized() throws Exception {
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

    @Test
    @WithMockUser
    void register_shouldAddProfileDetailsOfTheAccount() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "profilePicture", "test.jpg", "image/jpg", new byte[1000]
        );
        String username = "johndoe";
        String bio = "This is a sample bio";

        RegisterResponse registerResponse = RegisterResponse.builder()
                .username(username)
                .build();

        // when
        when(authService.register(username, bio, file)).thenReturn(registerResponse);

        // then
        mockMvc.perform(multipart("/api/auth/register")
                        .file(file)
                        .param("username", username)
                        .param("bio", bio)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(
                        document("registerSuccess",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                formParameters(
                                ),
                                requestParts(
                                        partWithName("profilePicture").description("The profile picture file")
                                )
                        )
                );
    }
}
