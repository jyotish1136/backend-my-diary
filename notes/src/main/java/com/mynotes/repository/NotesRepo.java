package com.mynotes.repository;

import com.mynotes.entities.Notes;
import com.mynotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepo extends JpaRepository<Notes,Long> {
    List<Notes> findByUser(User user);
}
