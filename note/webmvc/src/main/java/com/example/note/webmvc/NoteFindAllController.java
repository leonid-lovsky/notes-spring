package com.example.note.webmvc;

import com.example.note.domain.NoteFindAllPort;
import com.example.note.domain.NoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notes")
class NoteFindAllController {

    private final NoteFindAllPort noteFindAllPort;

    NoteFindAllController(NoteFindAllPort noteFindAllPort) {
        this.noteFindAllPort = noteFindAllPort;
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = noteFindAllPort.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

}
