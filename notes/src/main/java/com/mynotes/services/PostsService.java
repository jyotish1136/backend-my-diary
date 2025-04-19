package com.mynotes.services;

import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import com.mynotes.repository.PostsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class PostsService {
    @Autowired
    private PostsRepo postsRepo;
    @Autowired
    private UserService userService;
    public List<Post> getAllPosts(String username) {
        User user = userService.findByUsername(username);
        List<Post> all = postsRepo.findByUser(user);
        if (!all.isEmpty()){
            return postsRepo.findAll();
        }
        return null;
    }

    public Optional<Post> getPostById(Long id) {
        return postsRepo.findById(id);
    }

    public void savePost(Post note) {
        postsRepo.save(note);
    }
    public void saveNewPost(Post note, String username) {
        User user = userService.findByUsername(username);
        note.setDate(LocalDateTime.now());
        note.setUser(user);
        postsRepo.save(note);
    }
    public boolean deletePostById(Long id, User user) {
        try {
            boolean b = user.getNotes().removeIf(i -> Objects.equals(i.getId(), id));
            if (b){
                userService.saveUser(user);
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }
    public Post updatePostById(Long id, User user, Post post) {
        Post existingPost = postsRepo.findById(id).orElse(null);
        if (existingPost == null || !existingPost.getUser().getId().equals(user.getId())) {
            return post;
        }
        existingPost.setPrivacy(post.getPrivacy());
        existingPost.setDate(LocalDateTime.now());
        existingPost.setHashtags(post.getHashtags());
        if (post.getTitle() != null && !post.getTitle().isEmpty()) {
            existingPost.setTitle(post.getTitle());
        }
        if (post.getContent() != null && !post.getContent().isEmpty()) {
            existingPost.setContent(post.getContent());
        }
        return postsRepo.save(existingPost);
    }
    public List<Post> findAllPublicPosts(){
        return postsRepo.findPublicPosts();
    }
}
