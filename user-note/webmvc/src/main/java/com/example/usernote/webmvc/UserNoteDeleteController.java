package com.example.usernote.webmvc;

import java.util.UUID;

import com.example.usernote.contract.UserNoteExistsByUserIdAndNoteIdContract;
import com.example.usernote.contract.UserNoteRemoveContract;
import com.example.usernote.domain.UserNoteNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteDeleteController {

    private final UserNoteExistsByUserIdAndNoteIdContract userNoteExistsByUserIdAndNoteIdContract;

    private final UserNoteRemoveContract userNoteRemoveContract;

    UserNoteDeleteController(UserNoteExistsByUserIdAndNoteIdContract userNoteExistsByUserIdAndNoteIdContract,
            UserNoteRemoveContract userNoteRemoveContract) {
        this.userNoteExistsByUserIdAndNoteIdContract = userNoteExistsByUserIdAndNoteIdContract;
        this.userNoteRemoveContract = userNoteRemoveContract;
    }

    @DeleteMapping("/{userId}/{noteId}")
    ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        if (!this.userNoteExistsByUserIdAndNoteIdContract.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        this.userNoteRemoveContract.remove(userId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
