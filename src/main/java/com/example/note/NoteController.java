package com.example.note;

import com.example.note.payload.NoteRequest;
import com.example.note.payload.NoteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService service;

    @PostMapping
    ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    ResponseEntity<List<NoteResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponse> read(@PathVariable UUID id) {
        return ResponseEntity.ok(service.read(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> replace(@PathVariable UUID id, @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.ok(service.replace(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
