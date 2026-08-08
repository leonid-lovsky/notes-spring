package com.example.usernote.webflux;

import com.example.usernote.contract.reactive.UserNoteServiceReactiveInterface;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteController implements UserNoteControllerReactiveInterface {

    private final UserNoteServiceReactiveInterface userNoteService;

    UserNoteController(UserNoteServiceReactiveInterface userNoteService) {
        this.userNoteService = userNoteService;
    }

    @Override
    @GetMapping("/{userNoteId}/exists")
    public Mono<ResponseEntity<Boolean>> existsByUserNoteId(@PathVariable UUID userNoteId) {
        return this.userNoteService.existsByUserNoteId(userNoteId)
            .map((exists) -> ResponseEntity.status(HttpStatus.OK).body(exists));
    }

    @Override
    @GetMapping(path = "/exists", params = "userId")
    public Mono<ResponseEntity<Boolean>> existsByUserId(@RequestParam UUID userId) {
        return this.userNoteService.existsByUserId(userId)
            .map((exists) -> ResponseEntity.status(HttpStatus.OK).body(exists));
    }

    @Override
    @GetMapping(path = "/exists", params = "noteId")
    public Mono<ResponseEntity<Boolean>> existsByNoteId(@RequestParam UUID noteId) {
        return this.userNoteService.existsByNoteId(noteId)
            .map((exists) -> ResponseEntity.status(HttpStatus.OK).body(exists));
    }

    @Override
    @GetMapping(path = "/exists", params = {"userId", "noteId"})
    public Mono<ResponseEntity<Boolean>> existsByUserIdAndNoteId(@RequestParam UUID userId,
        @RequestParam UUID noteId) {
        return this.userNoteService.existsByUserIdAndNoteId(userId, noteId)
            .map((exists) -> ResponseEntity.status(HttpStatus.OK).body(exists));
    }

    @Override
    @PostMapping
    public Mono<ResponseEntity<UserNoteResponse>> add(@RequestBody UserNoteRequest request) {
        return this.userNoteService.add(request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.CREATED).body(userNote));
    }

    @Override
    @GetMapping("/{userNoteId}")
    public Mono<ResponseEntity<UserNoteResponse>> findByUserNoteId(@PathVariable UUID userNoteId) {
        return this.userNoteService.findByUserNoteId(userNoteId)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote));
    }

    @Override
    @GetMapping(params = "userId")
    public ResponseEntity<Flux<UserNoteResponse>> findByUserId(@RequestParam UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userNoteService.findByUserId(userId));
    }

    @Override
    @GetMapping(params = "noteId")
    public ResponseEntity<Flux<UserNoteResponse>> findByNoteId(@RequestParam UUID noteId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userNoteService.findByNoteId(noteId));
    }

    @Override
    @GetMapping(params = {"userId", "noteId"})
    public Mono<ResponseEntity<UserNoteResponse>> findByUserIdAndNoteId(@RequestParam UUID userId,
        @RequestParam UUID noteId) {
        return this.userNoteService.findByUserIdAndNoteId(userId, noteId)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote));
    }

    @Override
    @PutMapping("/{userNoteId}")
    public Mono<ResponseEntity<UserNoteResponse>> replaceByUserNoteId(@PathVariable UUID userNoteId,
        @RequestBody UserNoteRequest request) {
        return this.userNoteService.replaceByUserNoteId(userNoteId, request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote));
    }

    @Override
    @PutMapping
    public Mono<ResponseEntity<UserNoteResponse>> replaceByUserIdAndNoteId(@RequestParam UUID userId,
        @RequestParam UUID noteId, @RequestBody UserNoteRequest request) {
        return this.userNoteService.replaceByUserIdAndNoteId(userId, noteId, request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote));
    }

    @Override
    @PatchMapping("/{userNoteId}")
    public Mono<ResponseEntity<UserNoteResponse>> mergeByUserNoteId(@PathVariable UUID userNoteId,
        @RequestBody UserNoteRequest request) {
        return this.userNoteService.mergeByUserNoteId(userNoteId, request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote));
    }

    @Override
    @PatchMapping
    public Mono<ResponseEntity<UserNoteResponse>> mergeByUserIdAndNoteId(@RequestParam UUID userId,
        @RequestParam UUID noteId, @RequestBody UserNoteRequest request) {
        return this.userNoteService.mergeByUserIdAndNoteId(userId, noteId, request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote));
    }

    @Override
    @DeleteMapping("/{userNoteId}")
    public Mono<ResponseEntity<Void>> deleteByUserNoteId(@PathVariable UUID userNoteId) {
        return this.userNoteService.deleteByUserNoteId(userNoteId)
            .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build());
    }

    @Override
    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId) {
        return this.userNoteService.deleteByUserIdAndNoteId(userId, noteId)
            .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build());
    }
}
