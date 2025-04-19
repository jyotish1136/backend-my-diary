package com.mynotes.controllers;

import com.mynotes.entities.User;
import com.mynotes.services.GoogleOAuth2Service;
import com.mynotes.services.UserService;
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
    private UserService userService;
    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;

    @GetMapping("/google")
    public void loginWithGoogle(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = googleOAuth2Service.exchangeCodeForAccessToken(code);
        Map<String, Object> userInfo = googleOAuth2Service.fetchUserProfile(accessToken);
        String email = userInfo.get("email").toString();
        User existingUser = userService.findByEmail(email);
        String jwtToken;
        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setFirstname(userInfo.get("given_name").toString());
            newUser.setAvatar(userInfo.get("picture").toString());
            newUser.setRoles(List.of("USER"));
            newUser.setPassword(UUID.randomUUID().toString());

            userService.saveNewUser(newUser);
            jwtToken = jwtUtil.generateToken(newUser.getUsername());
        } else {
            jwtToken = jwtUtil.generateToken(existingUser.getUsername());
        }
        String redirectUri = "http://localhost:5173/?token=" + jwtToken;
        response.sendRedirect(redirectUri);
    }

}
