package com.mynotes.controllers;

import com.mynotes.DTO.CompleteSignupRequest;
import com.mynotes.DTO.PostDTO;
import com.mynotes.OAuth2Handler.TemporaryTokenService;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.services.PostsService;
import com.mynotes.services.UserDetailsServiceImpl;
import com.mynotes.services.UserService;
import com.mynotes.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.Optional;

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
    @Autowired
    private TemporaryTokenService temporaryTokenService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody CompleteSignupRequest request,
                                         HttpServletResponse response) {
        try {
            if (request==null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username, password, and email are required.");
            }
            String email = temporaryTokenService.getEmailFromToken(request.getTempToken())
                    .orElseThrow(() -> new RuntimeException("Invalid or expired token"));
            Optional<String> avatar = temporaryTokenService.getAvatar();
            User user = new User();
            user.setAvatar(avatar.get());
            user.setEmail(email);
            user.setUsername(request.getUsername());
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRoles(List.of("USER"));
            userService.saveNewUser(user);
            temporaryTokenService.invalidateToken(request.getTempToken());
            userService.saveNewUser(user);
            String jwtToken = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(jwtToken);
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
