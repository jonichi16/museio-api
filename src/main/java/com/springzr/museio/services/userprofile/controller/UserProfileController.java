package com.springzr.museio.services.userprofile.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.userprofile.model.response.UserProfileResponse;
import com.springzr.museio.services.userprofile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user profile-related endpoints.
 *
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);
    private final ProfileService profileService;

    /**
     * Retrieves user profile information by username.
     *
     * @param username the username to retrieve profile for
     * @return a {@link ResponseEntity} containing the user profile
     */
    @GetMapping("/{username}")
    public ResponseEntity<MSResponse<UserProfileResponse>> getUserProfile(
            @PathVariable String username
    ) {
        LOGGER.info("START: GET /api/auth/{}", username);

        UserProfileResponse profile = profileService.getProfileByUsername(username);
        HttpStatus status = HttpStatus.OK;

        MSResponse<UserProfileResponse> response = SuccessResponse.<UserProfileResponse>builder()
                .code(status.value())
                .message("Profile retrieved successfully")
                .data(profile)
                .build();

        LOGGER.info("END: GET /api/auth/{}", username);
        return ResponseEntity.ok(response);
    }
}
