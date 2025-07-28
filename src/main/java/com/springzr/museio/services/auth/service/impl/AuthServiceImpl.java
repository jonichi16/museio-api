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
import com.springzr.museio.services.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service class that implements {@link AuthService}.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenStore tokenStore;
    private final ProfileRepository profileRepository;
    private final TransactionalHandler transactionalHandler;
    private final UploadService uploadService;

    @Override
    public TokenResponse getToken(TokenRequest request) {

        String id = request.id();
        if (id == null) {
            throw new MSException("id is required", HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
        }

        String token = tokenStore.consume(id);
        if (token == null) {
            throw new MSException("Unauthorized", HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED);
        }
        String bearerType = "Bearer";

        return TokenResponse.builder()
                .accessToken(token)
                .bearerType(bearerType)
                .build();
    }

    @Override
    public RegisterResponse register(String username, String bio, MultipartFile profilePicture) {

        if (profileRepository.existsByUsername(username)) {
            throw new MSException(
                    "Username already taken",
                    HttpStatus.BAD_REQUEST,
                    ErrorCode.BAD_REQUEST
            );
        }

        String pp = "";
        String ppId = "";

        if (profilePicture != null) {
            UploadResult uploadResult = uploadService.upload(
                    profilePicture,
                    "/" + username + "/profile_picture",
                    "profile_picture"
            );

            pp = uploadResult.url();
            ppId = uploadResult.publicId();
        }

        Long accountId = TokenUtil.getAccountId();

        Profile profile = Profile.builder()
                .accountId(accountId)
                .username(username)
                .bio(bio)
                .profilePicture(pp)
                .profilePictureId(ppId)
                .build();

        transactionalHandler.runInTransaction(() -> profileRepository.save(profile));

        return RegisterResponse.builder()
                .username(username)
                .build();
    }
}

