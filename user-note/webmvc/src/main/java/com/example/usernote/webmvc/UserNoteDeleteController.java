package com.example.usernote.webmvc;

import java.util.UUID;

import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRemovePort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteDeleteController {

    private final UserNoteExistsByUserIdAndNoteIdPort userNoteExistsByUserIdAndNoteIdPort;

    private final UserNoteRemovePort userNoteRemovePort;

    UserNoteDeleteController(UserNoteExistsByUserIdAndNoteIdPort userNoteExistsByUserIdAndNoteIdPort,
            UserNoteRemovePort userNoteRemovePort) {
        this.userNoteExistsByUserIdAndNoteIdPort = userNoteExistsByUserIdAndNoteIdPort;
        this.userNoteRemovePort = userNoteRemovePort;
    }

    @DeleteMapping("/{userId}/{noteId}")
    ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        if (!this.userNoteExistsByUserIdAndNoteIdPort.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        this.userNoteRemovePort.remove(userId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
