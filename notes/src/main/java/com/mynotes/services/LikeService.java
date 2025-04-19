package com.mynotes.services;

import com.mynotes.DTO.LikeDTO;
import com.mynotes.entities.Like;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.repository.LikeRepo;
import com.mynotes.repository.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepo likeRepo;
    @Autowired
    private PostsRepo postRepo;

    @Autowired
    private PostsService postsService;
    @Autowired
    private UserService userService;

    @Transactional
    public String likeOrUnlike(LikeDTO like) {
        Long userId = like.getUserId();
        Long postId = like.getPostId();
        Optional<Post> post1 = postsService.getPostById(postId);
        Optional<User> user = userService.findById(userId);
        Post post = post1.get();
        Like byUserAndPost = likeRepo.findByUserAndPost(user.get(), post);
        if (byUserAndPost != null) {
            post.setLikeCount(post.getLikeCount() - 1);
            postsService.savePost(post);
            likeRepo.deleteByUserAndPost(user.get(), post);
            return "Post Unliked";
        }
        post.setLikeCount(post.getLikeCount() + 1);
        postsService.savePost(post);
        Like olike = new Like();
        olike.setUser(user.get());
        olike.setPost(post);
        olike.setLikedAt(LocalDateTime.now());
        likeRepo.save(olike);
        return "Post Liked";
    }


}
