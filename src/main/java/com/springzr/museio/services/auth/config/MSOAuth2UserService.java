package com.springzr.museio.services.auth.config;

import com.springzr.museio.services.auth.model.Account;
import com.springzr.museio.services.auth.repository.AccountRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MSOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest
    ) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                   Account newAccount = Account.builder()
                           .email(email)
                           .name(name)
                           .build();
                   return accountRepository.save(newAccount);
                });

        return MSOAuth2User.builder()
                .delegate(oAuth2User)
                .account(account)
                .build();
    }
}
