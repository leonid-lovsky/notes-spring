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

    private final RestNoteMapper restNoteMapper;

    @PostMapping
    ResponseEntity<ResponseNoteBody> create(@Valid @RequestBody RequestNoteBody requestNoteBody) {
        RequestNotePayload requestNotePayload = restNoteMapper.toRequestNotePayload(requestNoteBody);
        ResponseNotePayload responseNotePayload = createNoteService.create(requestNotePayload);
        ResponseNoteBody responseNoteBody = restNoteMapper.toResponseNoteBody(responseNotePayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseNoteBody);
    }

    @GetMapping
    ResponseEntity<List<ResponseNoteBody>> read() {
        List<ResponseNotePayload> responseNotePayload = readNoteService.read();
        List<ResponseNoteBody> responseNoteBody = responseNotePayload.stream().map(restNoteMapper::toResponseNoteBody).toList();
        return ResponseEntity.ok(responseNoteBody);
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseNoteBody> read(@PathVariable UUID id) {
        ResponseNotePayload responseNotePayload = readNoteService.read(id);
        ResponseNoteBody responseNoteBody = restNoteMapper.toResponseNoteBody(responseNotePayload);
        return ResponseEntity.ok(responseNoteBody);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ResponseNoteBody> update(@PathVariable UUID id, @Valid @RequestBody RequestNoteBody requestNoteBody) {
        RequestNotePayload requestNotePayload = restNoteMapper.toRequestNotePayload(requestNoteBody);
        ResponseNotePayload responseNotePayload = updateNoteService.update(id, requestNotePayload);
        ResponseNoteBody responseNoteBody = restNoteMapper.toResponseNoteBody(responseNotePayload);
        return ResponseEntity.ok(responseNoteBody);
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseNoteBody> replace(@PathVariable UUID id, @Valid @RequestBody RequestNoteBody requestNoteBody) {
        RequestNotePayload requestNotePayload = restNoteMapper.toRequestNotePayload(requestNoteBody);
        ResponseNotePayload responseNotePayload = replaceNoteService.replace(id, requestNotePayload);
        ResponseNoteBody responseNoteBody = restNoteMapper.toResponseNoteBody(responseNotePayload);
        return ResponseEntity.ok(responseNoteBody);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseNoteBody> delete(@PathVariable UUID id) {
        ResponseNotePayload responseNotePayload = deleteNoteService.delete(id);
        ResponseNoteBody responseNoteBody = restNoteMapper.toResponseNoteBody(responseNotePayload);
        return ResponseEntity.ok(responseNoteBody);
    }
}
