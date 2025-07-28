package com.springzr.museio.services.profile.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.profile.model.response.ProfileResponse;
import com.springzr.museio.services.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProfileControllerTest {
    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    @Test
    void getUserProfile_shouldReturnCorrectData() {
        // Given
        String username = "JohnDoe";
        String name = "John Doe";
        String email = "johndoe@mail.com";
        String bio = "Artist and dreamer.";
        String profilePicture = "https://sample-url.com";

        // Mock the service response
        when(profileService.getProfileByUsername(username)).thenReturn(
                ProfileResponse.builder()
                        .username(username)
                        .name(name)
                        .email(email)
                        .bio(bio)
                        .profilePicture(profilePicture)
                        .build()
        );

        // When
        ResponseEntity<MSResponse<ProfileResponse>> response = profileController.getUserProfile(username);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        MSResponse<ProfileResponse> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(200);
        assertThat(body.getMessage()).isEqualTo("Profile retrieved successfully");

        ProfileResponse profile = body.getData();
        assertThat(profile.username()).isEqualTo(username);
        assertThat(profile.name()).isEqualTo(name);
        assertThat(profile.email()).isEqualTo(email);
        assertThat(profile.bio()).isEqualTo(bio);
        assertThat(profile.profilePicture()).isEqualTo(profilePicture);
    }
}
