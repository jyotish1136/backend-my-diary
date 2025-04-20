package com.mynotes.controllers;

import com.mynotes.DTO.CommentDTO;
import com.mynotes.DTO.CommentDeleteDTO;
import com.mynotes.DTO.CommentRequestDTO;
import com.mynotes.entities.Comment;
import com.mynotes.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("comments")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/id/{postId}")
    public ResponseEntity<?> loadComment(@PathVariable Long postId){
        try {
            if (postId != null){
                List<Comment> comments = commentService.findByPostId(postId);
                List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
                    return new CommentDTO(
                            comment.getId(),
                            comment.getPostId(),
                            comment.getComment(),
                            comment.getUser().getUsername(),
                            comment.getUser().getAvatar(),
                            comment.getUser().getId(),
                            comment.getDate()
                    );
                }).toList();

                return ResponseEntity.ok(commentDTOs);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error loading comments: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentRequestDTO comment){
        try {
            if (comment!=null){
                Comment comment1 = commentService.saveComment(comment);
                return ResponseEntity.ok().body(comment1);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error posting comment: " + e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestBody CommentDeleteDTO comment){
        try {
            if (comment!=null){
                commentService.deleteComment(comment);
                return ResponseEntity.ok().body("Comment deleted");
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting comment: " + e.getMessage());
        }
    }
}
