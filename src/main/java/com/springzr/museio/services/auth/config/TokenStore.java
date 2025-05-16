package com.springzr.museio.services.auth.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {

    private final Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public void store(String state, String token) {
        tokenMap.put(state, token);
    }

    public String consume(String state) {
        return tokenMap.remove(state);
    }
}
