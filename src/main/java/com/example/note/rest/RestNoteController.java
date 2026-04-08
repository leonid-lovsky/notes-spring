package com.example.note.rest;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.rest.notes.path}")
@RequiredArgsConstructor
class RestNoteController {

    private final NoteService noteService;

    @PostMapping
    ResponseEntity<NotePayloadResponse> create(@Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteService.create(notePayloadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(notePayloadResponse);
    }

    @GetMapping
    ResponseEntity<List<NotePayloadResponse>> read() {
        List<NotePayloadResponse> notePayloadResponse = noteService.read();
        return ResponseEntity.ok(notePayloadResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<NotePayloadResponse> readById(@PathVariable UUID id) {
        NotePayloadResponse notePayloadResponse = noteService.readById(id);
        return ResponseEntity.ok(notePayloadResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NotePayloadResponse> updateById(@PathVariable UUID id, @Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteService.updateById(id, notePayloadRequest);
        return ResponseEntity.ok(notePayloadResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<NotePayloadResponse> replaceById(@PathVariable UUID id, @Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteService.replaceById(id, notePayloadRequest);
        return ResponseEntity.ok(notePayloadResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NotePayloadResponse> deleteById(@PathVariable UUID id) {
        NotePayloadResponse notePayloadResponse = noteService.deleteById(id);
        return ResponseEntity.ok(notePayloadResponse);
    }
}
