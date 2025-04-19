package com.mynotes.repository;

import com.mynotes.entities.Comment;
import com.mynotes.entities.Like;
import com.mynotes.entities.Post;
import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Long> {
}
