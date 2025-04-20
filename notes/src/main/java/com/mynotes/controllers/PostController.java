package com.mynotes.controllers;

import com.mynotes.DTO.PostDTO;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.repository.UserRepo;
import com.mynotes.services.PostsService;
import com.mynotes.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
@CrossOrigin
public class PostController {
    @Autowired
    private PostsService postsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id){
        Optional<Post> post = postsService.getPostById(id);
        if (post.isPresent()) {
            PostDTO map = modelMapper.map(post, PostDTO.class);
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getPosts() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            List<Post> allPosts = postsService.getAllPosts(username);
            if (allPosts != null && !allPosts.isEmpty()) {
                List<PostDTO> returnPosts = new ArrayList<>();
                for(var i : allPosts){
                    returnPosts.add(modelMapper.map(i, PostDTO.class));
                }
                return ResponseEntity.ok(returnPosts);
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching posts: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> postNote(@RequestBody Post note) {
        try {
            if (note == null || note.getTitle() == null || note.getContent() == null) {
                return ResponseEntity.badRequest().body("Note content is incomplete.");
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            postsService.saveNewPost(note,username);
            PostDTO map = modelMapper.map(note, PostDTO.class);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading post: " + e.getMessage());
        }
    }


    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateNote(@PathVariable Long id , @RequestBody Post post) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepo.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            Post post1 = postsService.updatePostById(id, user, post);
            PostDTO map = modelMapper.map(post1, PostDTO.class);
            if (map!=null) {
                return ResponseEntity.ok(map);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized or Note not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating posts: " + e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepo.findByUsername(username);
            if (id != null) {
                boolean b = postsService.deletePostById(id, user);
                if (b) return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting posts: " + e.getMessage());
        }
    }
}
