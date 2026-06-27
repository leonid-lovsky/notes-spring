package com.example.note.webmvc;

import com.example.note.domain.NoteFindByIdPort;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteFindByIdController {

    private final NoteFindByIdPort noteFindByIdPort;

    NoteFindByIdController(NoteFindByIdPort noteFindByIdPort) {
        this.noteFindByIdPort = noteFindByIdPort;
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        NoteResponse note = noteFindByIdPort.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(note);
    }

}
