package com.example.user.webflux;

import java.util.UUID;

import com.example.user.contract.reactive.UserServiceInterfaceReactive;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

@RestController
@RequestMapping("/users")
class UserController implements UserControllerInterfaceReactive {

    private final UserServiceInterfaceReactive userService;

    UserController(UserServiceInterfaceReactive userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/{id}/exists")
    public Mono<ResponseEntity<Boolean>> existsById(@PathVariable UUID id) {
        return this.userService.existsById(id).map((exists) -> ResponseEntity.status(HttpStatus.OK).body(exists));
    }

    @Override
    @PostMapping
    public Mono<ResponseEntity<UserResponse>> add(@RequestBody UserRequest request) {
        return this.userService.add(request).map((user) -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @Override
    @GetMapping
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable UUID id) {
        return this.userService.findById(id).map((user) -> ResponseEntity.status(HttpStatus.OK).body(user));
    }

    @Override
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> replace(@PathVariable UUID id, @RequestBody UserRequest request) {
        return this.userService.replace(id, request)
            .map((user) -> ResponseEntity.status(HttpStatus.OK).body(user));
    }

    @Override
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> merge(@PathVariable UUID id, @RequestBody UserRequest request) {
        return this.userService.merge(id, request).map((user) -> ResponseEntity.status(HttpStatus.OK).body(user));
    }

    @Override
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> remove(@PathVariable UUID id) {
        return this.userService.remove(id).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build());
    }

}
