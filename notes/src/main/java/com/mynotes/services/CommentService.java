package com.mynotes.services;

import com.mynotes.DTO.CommentDeleteDTO;
import com.mynotes.DTO.CommentRequestDTO;
import com.mynotes.entities.Comment;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public Comment saveComment(CommentRequestDTO dto) {
        Optional<Post> postOpt = postsService.getPostById(dto.getPostId());
        Optional<User> userOpt = userService.findById(dto.getUserId());

        if (postOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid postId or userId");
        }

        Post post = postOpt.get();
        User user = userOpt.get();
        post.setCommentCount(post.getCommentCount() + 1);
        postsService.savePost(post);
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPostId(post.getId());
        comment.setComment(dto.getComment());
        comment.setDate(LocalDateTime.now());
        return commentRepo.save(comment);
    }

    @Transactional
    public void deleteComment(CommentDeleteDTO comment) {
        Optional<Post> post = postsService.getPostById(comment.getPostId());
        Post post1 = post.get();
        post1.setCommentCount(post1.getCommentCount()-1);
        postsService.savePost(post1);
        commentRepo.deleteById(comment.getCommentId());
    }

    public List<Comment> findByPostId(Long id) {
        return commentRepo.findAllByPostId(id);
    }
}
