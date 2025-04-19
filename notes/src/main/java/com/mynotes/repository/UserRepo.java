package com.mynotes.repository;

import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String name);
}