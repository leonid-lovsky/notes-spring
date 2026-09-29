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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findUserById(@PathVariable("id") UUID id) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody UserRequest request) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> replaceUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable("id") UUID id) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
    }
}
