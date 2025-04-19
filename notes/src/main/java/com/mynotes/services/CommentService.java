package com.mynotes.services;

import com.mynotes.DTO.CommentDTO;
import com.mynotes.entities.Comment;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private PostsService postsService;
    @Autowired
    private UserService userService;
    @Transactional
    public Comment saveComment(CommentDTO commentDTO) {
        Optional<Post> post1 = postsService.getPostById(commentDTO.getPostId());
        Optional<User> user = userService.findById(commentDTO.getUserId());
        Comment comment = new Comment();
        comment.setUser(user.get());
        comment.setPost(post1.get());
        comment.setComment(commentDTO.getComment());
        comment.setDate(LocalDateTime.now());
        return commentRepo.save(comment);
    }
    public void deleteComment(Comment comment) {
        commentRepo.delete(comment);
    }
}
