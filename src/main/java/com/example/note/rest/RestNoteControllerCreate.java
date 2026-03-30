package com.example.note.rest;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class RestNoteControllerCreate {

    private final NoteServiceCreate noteServiceCreate;

    @PostMapping
    ResponseEntity<NotePayloadResponse> create(@Valid @RequestBody NotePayloadRequest notePayloadRequest) {
        NotePayloadResponse notePayloadResponse = noteServiceCreate.create(notePayloadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(notePayloadResponse);
    }
}
