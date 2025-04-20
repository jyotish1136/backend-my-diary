package com.mynotes.repository;

import com.mynotes.DTO.CommentDTO;
import com.mynotes.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
    @Transactional
    List<Comment> findAllByPostId(Long postId);
}