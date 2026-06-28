package com.example.usernote.webflux;

import com.example.usernote.contract.reactive.UserNoteAddContractReactive;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteCreateController {

    private final UserNoteAddContractReactive userNoteAddContractReactive;

    UserNoteCreateController(UserNoteAddContractReactive userNoteAddContractReactive) {
        this.userNoteAddContractReactive = userNoteAddContractReactive;
    }

    @PostMapping
    Mono<ResponseEntity<UserNoteResponse>> create(@RequestBody UserNoteRequest request) {
        return this.userNoteAddContractReactive.add(request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.CREATED).body(userNote));
    }

}
