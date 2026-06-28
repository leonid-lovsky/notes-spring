package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByNoteIdContractReactive;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByNoteIdController {

    private final UserNoteFindByNoteIdContractReactive userNoteFindByNoteIdContractReactive;

    UserNoteFindByNoteIdController(UserNoteFindByNoteIdContractReactive userNoteFindByNoteIdContractReactive) {
        this.userNoteFindByNoteIdContractReactive = userNoteFindByNoteIdContractReactive;
    }

    @GetMapping("/note/{noteId}")
    Flux<UserNoteResponse> findByNoteId(@PathVariable UUID noteId) {
        return this.userNoteFindByNoteIdContractReactive.findByNoteId(noteId);
    }

}
