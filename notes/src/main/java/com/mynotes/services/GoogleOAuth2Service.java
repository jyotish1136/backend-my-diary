package com.mynotes.services;

import com.mynotes.repository.UserRepo;
import com.mynotes.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
public class GoogleOAuth2Service {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.provider.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    public String exchangeCodeForAccessToken(String code) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", googleClientId);
        requestBody.put("client_secret", googleClientSecret);
        requestBody.put("code", code);
        requestBody.put("redirect_uri", googleRedirectUri);
        requestBody.put("grant_type", "authorization_code");
        ResponseEntity<Map> response = null;
        try {
            response = restTemplate.postForEntity(tokenUri, requestBody, Map.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        Map<String, Object> responseBody = response.getBody();
        return (String) responseBody.get("access_token");
    }

    public Map<String, Object> fetchUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }
}
