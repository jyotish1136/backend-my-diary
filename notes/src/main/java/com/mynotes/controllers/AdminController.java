package com.mynotes.controllers;

import com.mynotes.entities.User;
import com.mynotes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        List<User> allUsers = userService.getAllUsers();
        if (!allUsers.isEmpty()){
            return ResponseEntity.ok(allUsers);
        }
        return null;
    }
}
