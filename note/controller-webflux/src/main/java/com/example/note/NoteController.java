package com.example.note;

import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private static final String APPLICATION_MERGE_PATCH_JSON = "application/merge-patch+json";

    @GetMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> getNote(@PathVariable("id") UUID id) {
        return Mono.error(notImplemented());
    }

    @PostMapping
    public Mono<ResponseEntity<NoteResponse>> postNote(@RequestBody NoteRequest request) {
        return Mono.error(notImplemented());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<NoteResponse>> putNote(@PathVariable("id") UUID id, @RequestHeader(name = HttpHeaders.IF_MATCH, required = false) @Nullable String ifMatch, @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false) @Nullable String ifNoneMatch, @RequestBody NoteRequest request) {
        return Mono.error(notImplemented());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteNote(@PathVariable("id") UUID id, @RequestHeader(name = HttpHeaders.IF_MATCH, required = false) @Nullable String ifMatch) {
        return Mono.error(notImplemented());
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_MERGE_PATCH_JSON)
    public Mono<ResponseEntity<NoteResponse>> patchNote(@PathVariable("id") UUID id, @RequestHeader(name = HttpHeaders.IF_MATCH, required = false) @Nullable String ifMatch, @RequestBody JsonNode mergePatch) {
        return Mono.error(notImplemented());
    }

    private static ResponseStatusException notImplemented() {
        return new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
