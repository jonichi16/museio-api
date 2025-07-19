package com.springzr.museio.services.userprofile.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.userprofile.model.response.UserProfileResponse;
import com.springzr.museio.services.userprofile.service.ProfileService;
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
class UserProfileControllerTest {
    @Mock
    private ProfileService profileService;

    @InjectMocks
    private UserProfileController userProfileController;

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
                UserProfileResponse.builder()
                        .username(username)
                        .name(name)
                        .email(email)
                        .bio(bio)
                        .profilePicture(profilePicture)
                        .build()
        );

        // When
        ResponseEntity<MSResponse<UserProfileResponse>> response = userProfileController.getUserProfile(username);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        MSResponse<UserProfileResponse> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getCode()).isEqualTo(200);
        assertThat(body.getMessage()).isEqualTo("Profile retrieved successfully");

        UserProfileResponse profile = body.getData();
        assertThat(profile.username()).isEqualTo(username);
        assertThat(profile.name()).isEqualTo(name);
        assertThat(profile.email()).isEqualTo(email);
        assertThat(profile.bio()).isEqualTo(bio);
        assertThat(profile.profilePicture()).isEqualTo(profilePicture);
    }
}
