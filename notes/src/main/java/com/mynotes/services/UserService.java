package com.mynotes.services;

import com.mynotes.entities.User;
import com.mynotes.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Cacheable(value = "users" ,key = "#username")
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    @Cacheable(value = "email" ,key = "#email")
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public boolean existsByUsername(String name) {
        return userRepo.existsByUsername(name);
    }

    public Optional<User> findById(Long userId) {
        return userRepo.findById(userId);
    }
}
