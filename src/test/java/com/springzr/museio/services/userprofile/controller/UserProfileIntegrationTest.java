package com.springzr.museio.services.userprofile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.config.JwtAuthFilter;
import com.springzr.museio.services.userprofile.model.response.UserProfileResponse;
import com.springzr.museio.services.userprofile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserProfileController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")

class UserProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthFilter jwtAuthFilter; // <- THIS IS THE FIX

    @Test
    void getUserProfile_shouldReturn200Ok_whenUsernameExists() throws Exception {
        // given
        String username = "johndoe";
        UserProfileResponse response = UserProfileResponse.builder()
                .username(username)
                .name("John Doe")
                .email("johndoe@example.com")
                .bio("Digital Artist")
                .profilePicture("https://cdn.example.com/images/pfp.jpg")
                .build();

        // when
        when(profileService.getProfileByUsername(username)).thenReturn(response);

        // then
        mockMvc.perform(get("/api/auth/{username}", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("getUserProfileSuccess",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("Indicates whether the request was successful"),
                                fieldWithPath("code").description("HTTP status code"),
                                fieldWithPath("message").description("A human-readable success message"),
                                fieldWithPath("data.username").description("The user's username"),
                                fieldWithPath("data.name").description("The user's full name"),
                                fieldWithPath("data.email").description("The user's email address"),
                                fieldWithPath("data.bio").description("Short biography of the user"),
                                fieldWithPath("data.profilePicture").description("URL to the user's profile picture"),
                                fieldWithPath("timestamp").description("Timestamp when the response was generated")
                        )
                ));
    }

    @Test
    void getUserProfile_shouldReturn404NotFound_whenUsernameDoesNotExist() throws Exception {
        // given
        String username = "doesnotexist";

        when(profileService.getProfileByUsername(username))
                .thenThrow(new MSException(
                        "Profile not found",
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND
                ));

        // then
        mockMvc.perform(get("/api/auth/{username}", username))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("getUserProfileNotFound",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("Indicates whether the request was successful"),
                                fieldWithPath("errorCode").description("Application-specific error code"),
                                fieldWithPath("code").description("HTTP status code"),
                                fieldWithPath("message").description("A human-readable error message"),
                                fieldWithPath("timestamp").description("Timestamp when the error occurred")
                        )
                ));
    }
}