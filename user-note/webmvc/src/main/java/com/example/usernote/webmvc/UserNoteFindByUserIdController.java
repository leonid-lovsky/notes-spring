package com.example.usernote.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByUserIdController {

    private final UserNoteFindByUserIdContract userNoteFindByUserIdContract;

    UserNoteFindByUserIdController(UserNoteFindByUserIdContract userNoteFindByUserIdContract) {
        this.userNoteFindByUserIdContract = userNoteFindByUserIdContract;
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<UserNoteResponse>> findByUserId(@PathVariable UUID userId) {
        List<UserNoteResponse> userNotes = this.userNoteFindByUserIdContract.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

}
