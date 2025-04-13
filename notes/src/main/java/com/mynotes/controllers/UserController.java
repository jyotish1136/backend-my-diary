package com.mynotes.controllers;

import com.mynotes.entities.User;
import com.mynotes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> isUsernameAvailable(@RequestParam String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if (name != null) {
            boolean exists = userService.existsByUsername(username);
            return ResponseEntity.ok(!exists);
        }
        return ResponseEntity.badRequest().build();
    }
    @PutMapping
    public ResponseEntity<?> updateDetails(@RequestBody User user) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            if (user.getUsername() != null) existingUser.setUsername(user.getUsername());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getFirstname() != null) existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null) existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            userService.saveUser(existingUser);
            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
    @DeleteMapping
    public ResponseEntity<String> deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User byUsername = userService.findByUsername(username);
        if (byUsername == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userService.deleteUser(byUsername);
        return ResponseEntity.ok().body("Account deleted successfully");
    }
}
