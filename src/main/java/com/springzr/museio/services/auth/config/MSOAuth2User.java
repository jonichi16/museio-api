package com.springzr.museio.services.auth.config;

import com.springzr.museio.services.auth.model.Account;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MSOAuth2User implements OAuth2User {

    private final OAuth2User delegate;
    private final Account account;

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
