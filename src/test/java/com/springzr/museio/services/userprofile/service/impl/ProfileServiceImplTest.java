package com.springzr.museio.services.userprofile.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.model.Account;
import com.springzr.museio.services.auth.repository.AccountRepository;
import com.springzr.museio.services.userprofile.model.UserProfile;
import com.springzr.museio.services.userprofile.model.response.UserProfileResponse;
import com.springzr.museio.services.userprofile.repository.UserProfileRepository;
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
    private UserProfileRepository userProfileRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void getProfileByUsername_shouldReturnProfileResponse_whenUsernameExists() {
        // Arrange
        String username = "JohnDoe";
        Long accountId = 1L;

        Account mockAccount = Account.builder()
                .id(accountId)
                .name("John Doe")
                .email("johndoe@mail.com")
                .build();

        UserProfile mockProfile = UserProfile.builder()
                .accountId(accountId)
                .username(username)
                .bio("Digital Artist")
                .profilePicture("https://sample.com/pfp.jpg")
                .build();

        when(userProfileRepository.findByUsername(username)).thenReturn(Optional.of(mockProfile));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        UserProfileResponse response = profileService.getProfileByUsername(username);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("johndoe@mail.com");
        assertThat(response.bio()).isEqualTo("Digital Artist");
        assertThat(response.profilePicture()).isEqualTo("https://sample.com/pfp.jpg");

        verify(userProfileRepository, times(1)).findByUsername(username);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getProfileByUsername_shouldThrowException_whenUsernameNotFound() {
        // Arrange
        String username = "unknownUser";
        when(userProfileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        MSException exception = assertThrows(MSException.class, () ->
                profileService.getProfileByUsername(username)
        );

        assertThat(exception.getMessage()).isEqualTo("Profile not found");
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);

        verify(userProfileRepository, times(1)).findByUsername(username);
    }


}
