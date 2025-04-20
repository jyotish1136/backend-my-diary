package com.mynotes.repository;

import com.mynotes.DTO.LikeDTO;
import com.mynotes.entities.Like;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepo extends JpaRepository<Like,Long> {
    Like findByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);

//
//    @Query(value="Select a.user_id AS userId, a.post_id AS postId from likes  a where a.user_id= :userId AND a.post_id =:postid",nativeQuery = true)
//    LikeDTO findLikeData(@Param("postid") String postid , @Param("userId") String userId );
}
