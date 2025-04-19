package com.mynotes.repository;

import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepo extends JpaRepository<Post,Long> {
   List<Post> findByUser(User user);
   Post findById(long id);
    @Query(value = "SELECT * FROM posts_table p WHERE p.privacy = 'PUBLIC'",nativeQuery = true)
    List<Post> findPublicPosts();
}
