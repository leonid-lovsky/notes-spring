package com.example.note.rest;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceReadById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class RestNoteControllerReadById {

    private final NoteServiceReadById noteServiceReadById;

    @GetMapping("/{id}")
    ResponseEntity<NotePayloadResponse> readById(@PathVariable UUID id) {
        NotePayloadResponse notePayloadResponse = noteServiceReadById.readById(id);
        return ResponseEntity.ok(notePayloadResponse);
    }
}
