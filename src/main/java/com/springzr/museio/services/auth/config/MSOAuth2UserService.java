package com.springzr.museio.services.auth.config;

import com.springzr.museio.services.auth.model.Account;
import com.springzr.museio.services.auth.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom OAuth2UserService that handles user information after successful OAuth2 login.
 *
 * <p>If the user does not exist in the database, it creates a new {@link Account}.
 */
@Service
@RequiredArgsConstructor
public class MSOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;


    /**
     * Loads the OAuth2 user and ensures an account exists in the database.
     *
     * @param userRequest the user request containing OAuth2 access token and client info
     * @return an {@link OAuth2User} with linked account data
     * @throws OAuth2AuthenticationException if the OAuth2 login fails
     */
    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest
    ) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    Account newAccount = Account.builder()
                            .email(email)
                            .name(name)
                            .build();
                    return accountRepository.save(newAccount);
                });

        return MSOAuth2User.builder()
                .delegate(oauth2User)
                .account(account)
                .build();
    }
}
