package com.example.note.controller;

import com.example.note.*;
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

    private final NoteCreateService noteCreateService;
    private final NoteDeleteService noteDeleteService;
    private final NoteReadService noteReadService;
    private final NoteReplaceService noteReplaceService;
    private final NoteUpdateService noteUpdateService;

    @PostMapping
    ResponseEntity<NoteResponseModel> create(@Valid @RequestBody NoteRequestModel noteRequestModel) {
        NoteResponseModel noteResponseModel = noteCreateService.create(noteRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponseModel);
    }

    @GetMapping
    ResponseEntity<List<NoteResponseModel>> read() {
        List<NoteResponseModel> noteResponseModel = noteReadService.read();
        return ResponseEntity.ok(noteResponseModel);
    }

    @GetMapping("/{id}")
    ResponseEntity<NoteResponseModel> read(@PathVariable UUID id) {
        NoteResponseModel noteResponseModel = noteReadService.read(id);
        return ResponseEntity.ok(noteResponseModel);
    }

    @PatchMapping("/{id}")
    ResponseEntity<NoteResponseModel> update(@PathVariable UUID id, @Valid @RequestBody NoteRequestModel noteRequestModel) {
        NoteResponseModel noteResponseModel = noteUpdateService.update(id, noteRequestModel);
        return ResponseEntity.ok(noteResponseModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponseModel> replace(@PathVariable UUID id, @Valid @RequestBody NoteRequestModel noteRequestModel) {
        NoteResponseModel noteResponseModel = noteReplaceService.replace(id, noteRequestModel);
        return ResponseEntity.ok(noteResponseModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<NoteResponseModel> delete(@PathVariable UUID id) {
        NoteResponseModel noteResponseModel = noteDeleteService.delete(id);
        return ResponseEntity.ok(noteResponseModel);
    }
}
