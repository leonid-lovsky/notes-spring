package com.example.note.controller;

import com.example.note.NoteCreateService;
import com.example.note.NoteRequestModel;
import com.example.note.NoteResponseModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.notes.path}")
@RequiredArgsConstructor
class NoteRestController {

    private final NoteCreateService service;

    @PostMapping
    ResponseEntity<NoteResponseModel> create(@Valid @RequestBody NoteRequestModel requestBody) {
        NoteResponseModel responseBody = service.create(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    ResponseEntity<List<NoteResponseModel>> read() {
        List<NoteResponseModel> responseBody = service.read();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponseModel> read(@PathVariable UUID id) {
        NoteResponseModel responseBody = service.read(id);
        return ResponseEntity.ok(responseBody);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponseModel> update(@PathVariable UUID id, @Valid @RequestBody NoteRequestModel requestBody) {
        NoteResponseModel responseBody = service.update(id, requestBody);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponseModel> replace(@PathVariable UUID id, @Valid @RequestBody NoteRequestModel requestBody) {
        NoteResponseModel responseBody = service.replace(id, requestBody);
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NoteResponseModel> delete(@PathVariable UUID id) {
        NoteResponseModel responseBody = service.delete(id);
        return ResponseEntity.ok(responseBody);
    }
}
