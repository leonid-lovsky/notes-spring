package com.example.note;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @PostMapping
    public ResponseEntity<Void> createNote() {
        throw notImplemented();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> findNoteByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> replaceNoteByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateNoteByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    private static ResponseStatusException notImplemented() {
        return new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
