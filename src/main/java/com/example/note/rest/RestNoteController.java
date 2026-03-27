package com.example.note.rest;

import com.example.note.*;
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
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class RestNoteController {

    private final NoteServiceCreate noteServiceCreate;
    private final NoteServiceRead noteServiceRead;
    private final NoteServiceUpdate noteServiceUpdate;
    private final NoteServiceDelete noteServiceDelete;

    @PostMapping
    ResponseEntity<NotePayloadResponse> create(@Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteServiceCreate.create(notePayloadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(notePayloadResponse);
    }

    @GetMapping
    ResponseEntity<List<NotePayloadResponse>> read() {
        List<NotePayloadResponse> notePayloadResponse = noteServiceRead.read();
        return ResponseEntity.ok(notePayloadResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<NotePayloadResponse> read(@PathVariable UUID id) {
        NotePayloadResponse notePayloadResponse = noteServiceRead.read(id);
        return ResponseEntity.ok(notePayloadResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NotePayloadResponse> update(@PathVariable UUID id, @Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteServiceUpdate.update(id, notePayloadRequest);
        return ResponseEntity.ok(notePayloadResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<NotePayloadResponse> replace(@PathVariable UUID id, @Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteServiceUpdate.replace(id, notePayloadRequest);
        return ResponseEntity.ok(notePayloadResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NotePayloadResponse> delete(@PathVariable UUID id) {
        NotePayloadResponse notePayloadResponse = noteServiceDelete.delete(id);
        return ResponseEntity.ok(notePayloadResponse);
    }
}
