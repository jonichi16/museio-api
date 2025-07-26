package com.springzr.museio.services.profile.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.auth.model.Account;
import com.springzr.museio.services.auth.model.Profile;
import com.springzr.museio.services.auth.repository.AccountRepository;
import com.springzr.museio.services.auth.repository.ProfileRepository;
import com.springzr.museio.services.profile.model.response.ProfileResponse;
import com.springzr.museio.services.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service class that implements {@link ProfileService}.
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    @Override
    public ProfileResponse getProfileByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username)
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

        return ProfileResponse.builder()
                .username(profile.getUsername())
                .name(account.getName())
                .email(account.getEmail())
                .bio(profile.getBio())
                .profilePicture(profile.getProfilePicture())
                .build();
    }
}
