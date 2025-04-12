package com.mynotes.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class JwtUtil {
    private final String SECRET_KEY = Base64.getEncoder().encodeToString("SuperSecureSecretKeyForJWT".getBytes());
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(HashMap<String, Object> claims, String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();
    }
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build().parseClaimsJws(token)
                .getBody();
    }
    public boolean validateToken(String jwt, String username) {
        try {
            String s = extractUsername(jwt);
            return s.equals(username) && !isTokenExpired(jwt);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    private boolean isTokenExpired(String jwt) {
        return extractAllClaims(jwt).getExpiration().before(new Date());
    }

}
