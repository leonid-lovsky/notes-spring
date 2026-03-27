package com.example.note.presentation.rest;

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
class RestNoteController {

    private final CreateNoteService createNoteService;
    private final ReadNoteService readNoteService;
    private final UpdateNoteService updateNoteService;
    private final ReplaceNoteService replaceNoteService;
    private final DeleteNoteService deleteNoteService;

    @PostMapping
    ResponseEntity<ResponseNoteBody> create(@Valid @RequestBody RequestNoteBody requestNoteBody) {
        ResponseNoteBody responseNoteBody = createNoteService.create(requestNoteBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseNoteBody);
    }

    @GetMapping
    ResponseEntity<List<ResponseNoteBody>> read() {
        List<ResponseNoteBody> responseNoteBody = readNoteService.read();
        return ResponseEntity.ok(responseNoteBody);
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseNoteBody> read(@PathVariable UUID id) {
        ResponseNoteBody responseNoteBody = readNoteService.read(id);
        return ResponseEntity.ok(responseNoteBody);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ResponseNoteBody> update(@PathVariable UUID id, @Valid @RequestBody RequestNoteBody requestNoteBody) {
        ResponseNoteBody responseNoteBody = updateNoteService.update(id, requestNoteBody);
        return ResponseEntity.ok(responseNoteBody);
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseNoteBody> replace(@PathVariable UUID id, @Valid @RequestBody RequestNoteBody requestNoteBody) {
        ResponseNoteBody responseNoteBody = replaceNoteService.replace(id, requestNoteBody);
        return ResponseEntity.ok(responseNoteBody);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseNoteBody> delete(@PathVariable UUID id) {
        ResponseNoteBody responseNoteBody = deleteNoteService.delete(id);
        return ResponseEntity.ok(responseNoteBody);
    }
}
