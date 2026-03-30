package com.example.note.rest;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceDeleteById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class RestNoteControllerDeleteById {

    private final NoteServiceDeleteById noteServiceDeleteById;

    @DeleteMapping("/{id}")
    ResponseEntity<NotePayloadResponse> deleteById(@PathVariable UUID id) {
        NotePayloadResponse notePayloadResponse = noteServiceDeleteById.deleteById(id);
        return ResponseEntity.ok(notePayloadResponse);
    }
}
