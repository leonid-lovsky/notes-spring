package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByUserIdAndNoteIdController {

    private final UserNoteFindByUserIdAndNoteIdPort userNoteFindByUserIdAndNoteIdPort;

    UserNoteFindByUserIdAndNoteIdController(UserNoteFindByUserIdAndNoteIdPort userNoteFindByUserIdAndNoteIdPort) {
        this.userNoteFindByUserIdAndNoteIdPort = userNoteFindByUserIdAndNoteIdPort;
    }

    @GetMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        UserNoteResponse userNote = userNoteFindByUserIdAndNoteIdPort.findByUserIdAndNoteId(userId, noteId)
                .map(UserNoteResponse::from)
                .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        return ResponseEntity.status(HttpStatus.OK).body(userNote);
    }
}
