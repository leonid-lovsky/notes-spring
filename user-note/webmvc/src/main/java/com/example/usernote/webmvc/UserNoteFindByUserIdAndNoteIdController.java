package com.example.usernote.webmvc;

import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdAndNoteIdContract;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByUserIdAndNoteIdController {

    private final UserNoteFindByUserIdAndNoteIdContract userNoteFindByUserIdAndNoteIdContract;

    UserNoteFindByUserIdAndNoteIdController(
            UserNoteFindByUserIdAndNoteIdContract userNoteFindByUserIdAndNoteIdContract) {
        this.userNoteFindByUserIdAndNoteIdContract = userNoteFindByUserIdAndNoteIdContract;
    }

    @GetMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        UserNoteResponse userNote = this.userNoteFindByUserIdAndNoteIdContract.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        return ResponseEntity.status(HttpStatus.OK).body(userNote);
    }

}
