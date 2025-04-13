package com.mynotes.controllers;

import com.mynotes.entities.Notes;
import com.mynotes.entities.User;
import com.mynotes.repository.UserRepo;
import com.mynotes.services.NotesService;
import com.mynotes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin
public class NotesController {
    @Autowired
    private NotesService notesService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/id/{id}")
    public ResponseEntity<Notes> getNoteById(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Notes noteById = notesService.getNoteById(id);
        if (noteById != null) {
            return ResponseEntity.ok(noteById);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Notes>> getNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        List<Notes> allNotes = user.getNotes();
        if (allNotes != null && !allNotes.isEmpty()) {
            return ResponseEntity.ok(allNotes);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> postNote(@RequestBody Notes note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepo.findByUsername(username);

        if (note != null) {
            note.setDate(Instant.now());
            user.getNotes().add(note);
            note.setUser(user);
            notesService.saveNote(note);
            userService.saveUser(user);
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateNote(@PathVariable Long id, @RequestBody Notes note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        boolean updated = notesService.updateNotesById(id, user, note);
        if (updated) {
            return ResponseEntity.ok().body("Note updated successfully");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized or Note not found");
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepo.findByUsername(username);
        if (id != null) {
            boolean b = notesService.deleteNotesById(id, user);
            if (b) return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
