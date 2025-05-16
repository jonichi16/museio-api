package com.springzr.museio.services.auth.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Store and manage token generated upon successful authentication.
 *
 * <p>Upon successful authentication, token will be saved in a redis memory with the id.</p>
 */
@Component
@RequiredArgsConstructor
public class TokenStore {

    private static final long EXPIRATION_SECONDS = 60;
    private final StringRedisTemplate redisTemplate;


    /**
     * Store token to redis memory.
     *
     * @param id uuid of the jwt token
     * @param token jwt token generated upon authentication
     */
    public void store(String id, String token) {
        redisTemplate.opsForValue().set(id, token, Duration.ofSeconds(EXPIRATION_SECONDS));
    }

    /**
     * Retrieve jwt token based on id.
     *
     * @param id uuid of the jwt token to retrieve
     * @return jwt token
     */
    public String consume(String id) {
        String token = redisTemplate.opsForValue().get(id);
        if (token != null) {
            redisTemplate.delete(id);
        }
        return token;
    }
}
