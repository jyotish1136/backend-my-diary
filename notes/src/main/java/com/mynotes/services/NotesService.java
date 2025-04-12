package com.mynotes.services;

import com.mynotes.entities.Notes;
import com.mynotes.entities.User;
import com.mynotes.repository.NotesRepo;
import com.mynotes.repository.UserRepo;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class NotesService {
    @Autowired
    private NotesRepo notesRepo;
    @Autowired
    private UserService userService;
    public List<Notes> getAllNotes(String username) {
        User user = userService.findByUsername(username);
        List<Notes> all = notesRepo.findByUser(user);
        if (!all.isEmpty()){
            return notesRepo.findAll();
        }
        return null;
    }

    public Notes getNoteById(Long id) {
        return notesRepo.getReferenceById(id);
    }

    public void saveNote(Notes note) {
        notesRepo.save(note);
    }
    public boolean deleteNotesById(Long id, User user) {
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
    public boolean updateNotesById(Long id, User user, Notes note) {
        Notes existingNote = notesRepo.findById(id).orElse(null);

        if (existingNote == null || !existingNote.getUser().getId().equals(user.getId())) {
            return false;
        }
        existingNote.setDate(Instant.now());

        if (note.getTitle() != null && !note.getTitle().isEmpty()) {
            existingNote.setTitle(note.getTitle());
        }

        if (note.getContent() != null && !note.getContent().isEmpty()) {
            existingNote.setContent(note.getContent());
        }
        notesRepo.save(existingNote);
        return true;
    }

}
