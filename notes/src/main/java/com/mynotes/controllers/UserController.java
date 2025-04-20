package com.mynotes.controllers;

import com.mynotes.DTO.UserDTO;
import com.mynotes.entities.User;
import com.mynotes.services.UserService;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            if (userDTO != null) return ResponseEntity.ok(userDTO);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching user: " + e.getMessage());
        }
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
            UserDTO userDTO = modelMapper.map(existingUser, UserDTO.class);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user details");
        }
    }
    @DeleteMapping
    public ResponseEntity<String> deleteAccount() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User byUsername = userService.findByUsername(username);
            if (byUsername == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            userService.deleteUser(byUsername);
            return ResponseEntity.ok().body("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }
}
