package com.example.note.webmvc;

import java.util.List;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.domain.NoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteFindAllController {

    private final NoteFindAllContract noteFindAllContract;

    NoteFindAllController(NoteFindAllContract noteFindAllContract) {
        this.noteFindAllContract = noteFindAllContract;
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = this.noteFindAllContract.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

}
