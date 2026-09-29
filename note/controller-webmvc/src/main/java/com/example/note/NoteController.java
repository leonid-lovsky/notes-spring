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
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private static final String APPLICATION_MERGE_PATCH_JSON = "application/merge-patch+json";

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @PostMapping
    public ResponseEntity<NoteResponse> postNote(@RequestBody NoteRequest request) {
        throw notImplemented();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> putNote(@PathVariable("id") UUID id, @RequestHeader(name = HttpHeaders.IF_MATCH, required = false) @Nullable String ifMatch, @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false) @Nullable String ifNoneMatch, @RequestBody NoteRequest request) {
        throw notImplemented();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") UUID id, @RequestHeader(name = HttpHeaders.IF_MATCH, required = false) @Nullable String ifMatch) {
        throw notImplemented();
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_MERGE_PATCH_JSON)
    public ResponseEntity<NoteResponse> patchNote(@PathVariable("id") UUID id, @RequestHeader(name = HttpHeaders.IF_MATCH, required = false) @Nullable String ifMatch, @RequestBody JsonNode mergePatch) {
        throw notImplemented();
    }

    private static ResponseStatusException notImplemented() {
        return new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
