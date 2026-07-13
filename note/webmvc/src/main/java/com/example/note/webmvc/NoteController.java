package com.example.note.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.note.contract.NoteServiceInterface;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteController implements NoteControllerInterface {

    private final NoteServiceInterface noteService;

    NoteController(NoteServiceInterface noteService) {
        this.noteService = noteService;
    }

    @Override
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID id) {
        Boolean response = this.noteService.existsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<NoteResponse> add(@RequestBody NoteRequest request) {
        NoteResponse response = this.noteService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<NoteResponse>> findAll() {
        List<NoteResponse> response = this.noteService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> findById(@PathVariable UUID id) {
        NoteResponse response = this.noteService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> replace(@PathVariable UUID id, @RequestBody NoteRequest request) {
        NoteResponse response = this.noteService.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<NoteResponse> merge(@PathVariable UUID id, @RequestBody NoteRequest request) {
        NoteResponse response = this.noteService.merge(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<NoteResponse> remove(@PathVariable UUID id) {
        NoteResponse response = this.noteService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
