package com.mynotes.OAuth2Handler;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemporaryTokenService {
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();
    public String createToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);
        return token;
    }
    public void setAvatar(String avatar){
        tokenStore.put("avatar", avatar);
    }
    public Optional<String> getEmailFromToken(String token) {
        return Optional.ofNullable(tokenStore.get(token));
    }
    public Optional<String> getAvatar() {
        return Optional.ofNullable(tokenStore.get("avatar"));
    }
    public void invalidateToken(String token) {
        tokenStore.remove(token);
    }
}

