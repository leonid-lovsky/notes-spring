package com.example.note.rest;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceReplaceById;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.rest.notes.path}")
@RequiredArgsConstructor
class RestNoteControllerReplaceById {

    private final NoteServiceReplaceById noteServiceReplaceById;

    @PutMapping("/{id}")
    ResponseEntity<NotePayloadResponse> replaceById(@PathVariable UUID id, @Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteServiceReplaceById.replaceById(id, notePayloadRequest);
        return ResponseEntity.ok(notePayloadResponse);
    }
}
