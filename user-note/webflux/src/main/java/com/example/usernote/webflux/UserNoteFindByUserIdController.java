package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdContractReactive;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByUserIdController {

    private final UserNoteFindByUserIdContractReactive userNoteFindByUserIdContractReactive;

    UserNoteFindByUserIdController(UserNoteFindByUserIdContractReactive userNoteFindByUserIdContractReactive) {
        this.userNoteFindByUserIdContractReactive = userNoteFindByUserIdContractReactive;
    }

    @GetMapping("/user/{userId}")
    Flux<UserNoteResponse> findByUserId(@PathVariable UUID userId) {
        return this.userNoteFindByUserIdContractReactive.findByUserId(userId);
    }

}
