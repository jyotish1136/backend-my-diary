package com.mynotes.repository;

import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PostsRepo extends JpaRepository<Post,Long> {
   List<Post> findByUser(User user);
    @Query(value = """
    SELECT p.* FROM posts_table p
    WHERE p.privacy = 'PUBLIC'
    AND p.user_id <> :userId
    """, nativeQuery = true)
    ArrayList<Post> findAllPublicPostsExcludingUserId(@Param("userId") Long userId);
}
