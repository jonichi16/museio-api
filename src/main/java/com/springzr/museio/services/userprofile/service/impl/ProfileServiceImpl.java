package com.springzr.museio.services.userprofile.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.model.Account;
import com.springzr.museio.services.auth.repository.AccountRepository;
import com.springzr.museio.services.userprofile.model.UserProfile;
import com.springzr.museio.services.userprofile.model.response.UserProfileResponse;
import com.springzr.museio.services.userprofile.repository.UserProfileRepository;
import com.springzr.museio.services.userprofile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class that implements {@link ProfileService}.
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AccountRepository accountRepository;

    @Override
    public UserProfileResponse getProfileByUsername(String username) {
        UserProfile profile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new MSException(
                        "Profile not found",
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND
                ));

        Account account = accountRepository.findById(profile.getAccountId())
                .orElseThrow(() -> new MSException(
                        "Account not found",
                        HttpStatus.NOT_FOUND,
                        ErrorCode.NOT_FOUND
                ));

        return UserProfileResponse.builder()
                .username(profile.getUsername())
                .name(account.getName())
                .email(account.getEmail())
                .bio(profile.getBio())
                .profilePicture(profile.getProfilePicture())
                .build();
    }
}
