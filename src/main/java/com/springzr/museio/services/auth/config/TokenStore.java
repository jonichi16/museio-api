package com.springzr.museio.services.auth.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStore {

    private final StringRedisTemplate redisTemplate;

    private static final long EXPIRATION_SECONDS = 60;

    public void store(String id, String token) {
        redisTemplate.opsForValue().set(id, token, Duration.ofSeconds(EXPIRATION_SECONDS));
    }

    public String consume(String id) {
        String token = redisTemplate.opsForValue().get(id);
        if (token != null) {
            redisTemplate.delete(id);
        }
        return token;
    }
}
