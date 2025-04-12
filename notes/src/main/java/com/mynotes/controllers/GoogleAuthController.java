package com.mynotes.controllers;

import com.mynotes.entities.User;
import com.mynotes.repository.UserRepo;
import com.mynotes.services.GoogleOAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import com.mynotes.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/oauth2")
public class GoogleAuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;
    @GetMapping("/google")
    public void loginWithGoogle(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = googleOAuth2Service.exchangeCodeForAccessToken(code);
        Map<String, Object> userInfo = googleOAuth2Service.fetchUserProfile(accessToken);
        String email = userInfo.get("email").toString();
        String name = userInfo.get("name").toString();
        User existingUser = userRepo.findByEmail(email);
        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setFirstname(name);
            newUser.setRoles(List.of("USER"));
            newUser.setPassword(UUID.randomUUID().toString());
            userRepo.save(newUser);
        }
        String jwtToken = jwtUtil.generateToken(email);
        String redirectUri = "http://localhost:5173/home?token=" + jwtToken;
        response.sendRedirect(redirectUri);
    }
}
