package com.mynotes.controllers;

import com.mynotes.DTO.PostDTO;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.services.PostsService;
import com.mynotes.services.UserDetailsServiceImpl;
import com.mynotes.services.UserService;
import com.mynotes.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostsService postsService;
    @GetMapping("/health-check")
    public String healthCheck() {
        return "All is well";
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            if (user == null || user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username, password, and email are required.");
            }
            user.setRoles(List.of("USER"));
            userService.saveNewUser(user);
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error in signup: " + e.getMessage());
        }
    }
    @GetMapping("/check-username")
    public ResponseEntity<?> isUsernameAvailable(@RequestParam String username) {
        try {
            if (username != null) {
                boolean exists = userService.existsByUsername(username);
                return ResponseEntity.ok(!exists);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking username: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required.");
        }
        try {
            String username = user.getUsername().toLowerCase();
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticate.getName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwt);
        } catch (AuthenticationException e) {
            log.warn("Invalid login attempt for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}
