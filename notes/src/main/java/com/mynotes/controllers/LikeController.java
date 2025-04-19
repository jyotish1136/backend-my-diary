package com.mynotes.controllers;

import com.mynotes.DTO.LikeDTO;
import com.mynotes.services.LikeService;
import com.mynotes.services.PostsService;
import com.mynotes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("likes")
@CrossOrigin
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostsService postsService;
    @PostMapping
    public ResponseEntity<?> likeOrUnlike(@RequestBody LikeDTO like){
        if (like!=null){
            String message = likeService.likeOrUnlike(like);
            return ResponseEntity.ok().body(message);
        }
        return ResponseEntity.badRequest().build();
    }
}
