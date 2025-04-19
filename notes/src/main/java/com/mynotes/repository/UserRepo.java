package com.mynotes.repository;

import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    @Transactional
    User findByUsername(String username);
    @Transactional
    User findByEmail(String email);
    @Transactional
    boolean existsByUsername(String name);
}
