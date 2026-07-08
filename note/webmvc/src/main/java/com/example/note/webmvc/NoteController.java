package com.example.note.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.note.contract.NoteContract;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteController {

    private final NoteContract noteContract;

    NoteController(NoteContract noteContract) {
        this.noteContract = noteContract;
    }

    @PostMapping
    ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        NoteResponse note = this.noteContract.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> notes = this.noteContract.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        NoteResponse note = this.noteContract.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(note);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        if (!this.noteContract.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        NoteResponse updated = this.noteContract.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!this.noteContract.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        this.noteContract.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
