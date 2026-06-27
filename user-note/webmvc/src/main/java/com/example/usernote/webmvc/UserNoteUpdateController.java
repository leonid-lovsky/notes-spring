package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteReplacePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteUpdateController {

    private final UserNoteExistsByUserIdAndNoteIdPort userNoteExistsByUserIdAndNoteIdPort;
    private final UserNoteReplacePort userNoteReplacePort;

    UserNoteUpdateController(UserNoteExistsByUserIdAndNoteIdPort userNoteExistsByUserIdAndNoteIdPort,
                             UserNoteReplacePort userNoteReplacePort) {
        this.userNoteExistsByUserIdAndNoteIdPort = userNoteExistsByUserIdAndNoteIdPort;
        this.userNoteReplacePort = userNoteReplacePort;
    }

    @PutMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId,
                                            @RequestBody UserNoteRequest request) {
        if (!userNoteExistsByUserIdAndNoteIdPort.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNote updated = new UserNote(userId, noteId, request.role());
        userNoteReplacePort.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(UserNoteResponse.from(updated));
    }
}
