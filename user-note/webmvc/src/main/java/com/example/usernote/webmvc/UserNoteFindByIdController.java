package com.example.usernote.webmvc;

import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByIdContract;
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
class UserNoteFindByIdController {

    private final UserNoteFindByIdContract userNoteFindByIdContract;

    UserNoteFindByIdController(UserNoteFindByIdContract userNoteFindByIdContract) {
        this.userNoteFindByIdContract = userNoteFindByIdContract;
    }

    @GetMapping("/{id}")
    ResponseEntity<UserNoteResponse> findById(@PathVariable UUID id) {
        UserNoteResponse userNote = this.userNoteFindByIdContract.findById(id)
            .orElseThrow(() -> new UserNoteNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(userNote);
    }

}
