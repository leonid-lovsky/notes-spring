package com.example.note.rest;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceRead;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class RestNoteControllerRead {

    private final NoteServiceRead noteServiceRead;

    @GetMapping
    ResponseEntity<List<NotePayloadResponse>> read() {
        List<NotePayloadResponse> notePayloadResponse = noteServiceRead.read();
        return ResponseEntity.ok(notePayloadResponse);
    }
}
