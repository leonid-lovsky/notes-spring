package com.example.usernote.webmvc;

import java.util.UUID;

import com.example.usernote.contract.UserNoteExistsByUserIdAndNoteIdContract;
import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteUpdateController {

    private final UserNoteExistsByUserIdAndNoteIdContract userNoteExistsByUserIdAndNoteIdContract;

    private final UserNoteReplaceContract userNoteReplaceContract;

    UserNoteUpdateController(UserNoteExistsByUserIdAndNoteIdContract userNoteExistsByUserIdAndNoteIdContract,
            UserNoteReplaceContract userNoteReplaceContract) {
        this.userNoteExistsByUserIdAndNoteIdContract = userNoteExistsByUserIdAndNoteIdContract;
        this.userNoteReplaceContract = userNoteReplaceContract;
    }

    @PutMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId,
            @RequestBody UserNoteRequest request) {
        if (!this.userNoteExistsByUserIdAndNoteIdContract.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNoteResponse updated = this.userNoteReplaceContract.replace(userId, noteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

}
