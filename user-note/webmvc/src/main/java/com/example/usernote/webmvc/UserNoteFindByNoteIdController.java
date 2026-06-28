package com.example.usernote.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByNoteIdContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByNoteIdController {

    private final UserNoteFindByNoteIdContract userNoteFindByNoteIdContract;

    UserNoteFindByNoteIdController(UserNoteFindByNoteIdContract userNoteFindByNoteIdContract) {
        this.userNoteFindByNoteIdContract = userNoteFindByNoteIdContract;
    }

    @GetMapping("/note/{noteId}")
    ResponseEntity<List<UserNoteResponse>> findByNoteId(@PathVariable UUID noteId) {
        List<UserNoteResponse> userNotes = this.userNoteFindByNoteIdContract.findByNoteId(noteId);
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

}
