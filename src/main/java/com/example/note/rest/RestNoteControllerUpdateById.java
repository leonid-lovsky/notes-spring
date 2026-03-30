package com.example.note.rest;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceUpdateById;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class RestNoteControllerUpdateById {

    private final NoteServiceUpdateById noteServiceUpdateById;

    @PatchMapping("/{id}")
    ResponseEntity<NotePayloadResponse> updateById(@PathVariable UUID id, @Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteServiceUpdateById.updateById(id, notePayloadRequest);
        return ResponseEntity.ok(notePayloadResponse);
    }
}
