package com.mynotes.controllers;

import com.mynotes.DTO.CommentDTO;
import com.mynotes.entities.Comment;
import com.mynotes.services.CommentService;
import com.mynotes.services.PostsService;
import com.mynotes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO){
        if (commentDTO!=null){
            Comment comment = commentService.saveComment(commentDTO);
            return ResponseEntity.ok().body(comment);
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestBody Comment comment){
        System.out.println(comment);
        if (comment!=null){
            commentService.deleteComment(comment);
            return ResponseEntity.ok().body("Comment deleted");
        }
        return ResponseEntity.badRequest().build();
    }
}
