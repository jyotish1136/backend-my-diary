package com.mynotes.repository;

import com.mynotes.entities.Like;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepo extends JpaRepository<Like,Long> {
    @Transactional
    Like findByUserAndPost(User user, Post post);
    @Transactional
    void deleteByUserAndPost(User user, Post post);
}
