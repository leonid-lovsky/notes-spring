package com.example.user;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public Mono<ResponseEntity<Void>> createUser() {
        return Mono.error(notImplemented());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Void>> findUserByID(@PathVariable("id") UUID id) {
        return Mono.error(notImplemented());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> replaceUserByID(@PathVariable("id") UUID id) {
        return Mono.error(notImplemented());
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateUserByID(@PathVariable("id") UUID id) {
        return Mono.error(notImplemented());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserByID(@PathVariable("id") UUID id) {
        return Mono.error(notImplemented());
    }

    private static ResponseStatusException notImplemented() {
        return new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
