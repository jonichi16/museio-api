package com.springzr.museio.services.auth.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.libs.common.util.TokenUtil;
import com.springzr.museio.libs.common.util.TransactionalHandler;
import com.springzr.museio.libs.upload.model.UploadResult;
import com.springzr.museio.libs.upload.service.UploadService;
import com.springzr.museio.services.auth.config.TokenStore;
import com.springzr.museio.services.auth.model.Profile;
import com.springzr.museio.services.auth.model.request.TokenRequest;
import com.springzr.museio.services.auth.model.response.RegisterResponse;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.auth.repository.ProfileRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AuthServiceImplTest {

    @Mock
    private TokenStore tokenStore;
    @Mock
    private UploadService uploadService;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private TransactionalHandler transactionalHandler;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void getToken_shouldReturnTokenResponse() {
        // given
        String id = "sampleId";
        String accessToken = "accessToken";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        // when
        when(tokenStore.consume(id)).thenReturn(accessToken);
        TokenResponse token = authServiceImpl.getToken(tokenRequest);

        // then
        verify(tokenStore, times(1)).consume(id);
        assertThat(token.accessToken()).isEqualTo(accessToken);
        assertThat(token.bearerType()).isEqualTo("Bearer");
    }

    @Test
    void getToken_whenIdIsNull_shouldThrow400BadRequest() {
        // given
        TokenRequest tokenRequest = TokenRequest.builder()
                .build();

        // when
        // then
        assertThatThrownBy(() -> authServiceImpl.getToken(tokenRequest))
                .isInstanceOf(MSException.class)
                .hasMessage("id is required")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.BAD_REQUEST);
        verify(tokenStore, never()).consume(null);
    }

    @Test
    void getToken_whenTokenStoreReturnNull_shouldThrow400Unauthorized() {
        // given
        String id = "sampleId";
        TokenRequest tokenRequest = TokenRequest.builder()
                .id(id)
                .build();

        // when
        when(tokenStore.consume(id)).thenReturn(null);

        // then
        assertThatThrownBy(() -> authServiceImpl.getToken(tokenRequest))
                .isInstanceOf(MSException.class)
                .hasMessage("Unauthorized")
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNAUTHORIZED)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.UNAUTHORIZED);
    }

    @Test
    void register_shouldReturnRegisterResponse() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "profilePicture", "test.jpg", "image/jpg", new byte[1000]
        );
        String username = "johndoe";
        String bio = "This is a sample bio";
        String folderName = "/" + username + "/profile_picture";
        String publicId = "profile_picture";

        RegisterResponse registerResponse = RegisterResponse.builder()
                .username(username)
                .build();

        UploadResult uploadResult = UploadResult.builder()
                .url("https://sample-url.com")
                .publicId("sample-image")
                .build();

        Profile profile = Profile.builder()
                .accountId(1L)
                .username(username)
                .bio(bio)
                .profilePicture(uploadResult.url())
                .profilePictureId(uploadResult.publicId())
                .build();

        try (MockedStatic<TokenUtil> mockedTokenUtil = Mockito.mockStatic(TokenUtil.class)) {
            // when
            when(uploadService.upload(file, folderName, publicId)).thenReturn(uploadResult);
            when(profileRepository.save(any(Profile.class))).thenReturn(profile);
            mockedTokenUtil.when(TokenUtil::getAccountId).thenReturn(1L);
            doAnswer(invocation -> {
                Runnable action = invocation.getArgument(0);
                action.run();
                return null;
            }).when(transactionalHandler).runInTransaction(any(Runnable.class));
            RegisterResponse response = authServiceImpl.register(username, bio, file);

            // then
            mockedTokenUtil.verify(TokenUtil::getAccountId, times(1));
            verify(uploadService, times(1)).upload(file, folderName, publicId);
            verify(profileRepository, times(1)).save(profile);
            verify(transactionalHandler, times(1)).runInTransaction(any(Runnable.class));
            assertThat(response).isEqualTo(registerResponse);
        }
    }

    @Test
    void register_whenNullProfilePicture_shouldSaveBlankProfilePictures() {
        // given
        String username = "johndoe";
        String bio = "This is a sample bio";

        RegisterResponse registerResponse = RegisterResponse.builder()
                .username(username)
                .build();

        Profile profile = Profile.builder()
                .accountId(1L)
                .username(username)
                .bio(bio)
                .profilePicture("")
                .profilePictureId("")
                .build();

        try (MockedStatic<TokenUtil> mockedTokenUtil = Mockito.mockStatic(TokenUtil.class)) {
            // when
            when(profileRepository.save(any(Profile.class))).thenReturn(profile);
            mockedTokenUtil.when(TokenUtil::getAccountId).thenReturn(1L);
            doAnswer(invocation -> {
                Runnable action = invocation.getArgument(0);
                action.run();
                return null;
            }).when(transactionalHandler).runInTransaction(any(Runnable.class));
            RegisterResponse response = authServiceImpl.register(username, bio, null);

            // then
            mockedTokenUtil.verify(TokenUtil::getAccountId, times(1));
            verifyNoInteractions(uploadService);
            verify(profileRepository, times(1)).save(profile);
            verify(transactionalHandler, times(1)).runInTransaction(any(Runnable.class));
            assertThat(response).isEqualTo(registerResponse);
        }
    }

    @Test
    void register_whenUsernameExists_shouldThrowMSException() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "profilePicture", "test.jpg", "image/jpg", new byte[1000]
        );
        String username = "johndoe";
        String bio = "This is a sample bio";

        // when
        when(profileRepository.existsByUsername(username)).thenReturn(true);

        // then
        assertThatThrownBy(() -> authServiceImpl.register(username, bio, file))
                .isInstanceOf(MSException.class)
                .hasMessage("Username already taken");
        verify(profileRepository, times(1)).existsByUsername(username);
    }

}
