package com.springzr.museio.services.auth.config;

import com.springzr.museio.services.auth.model.Account;
import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
public record MSOAuth2User(OAuth2User delegate, Account account) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return account.getId().toString();
    }
}
