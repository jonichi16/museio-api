package com.springzr.museio.services.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures application security using Spring Security.
 *
 * <p>Includes JWT authentication and OAuth2 login handling.</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MSOAuth2UserService msoAuth2UserService;
    private final OAuth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthFilter jwtAuthFilter;


    /**
     * Defines the security filter chain for HTTP requests.
     *
     * <p>Disables CSRF, sets stateless sessions, allows unauthenticated access to /auth/**
     * endpoints, and requires authentication for all others. It also configures JWT filtering and
     * OAuth2 login with custom user service and success handler.</p>
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(msoAuth2UserService)
                        )
                        .successHandler(oauth2SuccessHandler));
        return http.build();
    }
}
