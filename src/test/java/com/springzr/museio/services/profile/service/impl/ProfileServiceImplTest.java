package com.springzr.museio.services.profile.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.model.Account;
import com.springzr.museio.services.profile.model.Profile;
import com.springzr.museio.services.profile.model.response.ProfileResponse;
import com.springzr.museio.services.profile.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProfileServiceImplTest {
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void getProfileByUsername_shouldReturnProfileResponse_whenUsernameExists() {
        // Arrange
        String username = "JohnDoe";
        Account mockAccount = Account.builder()
                .name("John Doe")
                .email("johndoe@mail.com")
                .build();

        Profile mockProfile = Profile.builder()
                .username(username)
                .bio("Digital Artist")
                .profilePicture("https://sample.com/pfp.jpg")
                .account(mockAccount)
                .build();

        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(mockProfile));

        // Act
        ProfileResponse response = profileService.getProfileByUsername(username);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("johndoe@mail.com");
        assertThat(response.bio()).isEqualTo("Digital Artist");
        assertThat(response.profilePicture()).isEqualTo("https://sample.com/pfp.jpg");

        verify(profileRepository, times(1)).findByUsername(username);
    }

    @Test
    void getProfileByUsername_shouldThrowException_whenUsernameNotFound() {
        // Arrange
        String username = "unknownUser";
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        MSException exception = assertThrows(MSException.class, () ->
                profileService.getProfileByUsername(username)
        );

        assertThat(exception.getMessage()).isEqualTo("Profile not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);

        verify(profileRepository, times(1)).findByUsername(username);
    }


}
