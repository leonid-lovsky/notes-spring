package com.example.usernote.webmvc;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteCreateController {

    private final UserNoteAddContract userNoteAddContract;

    UserNoteCreateController(UserNoteAddContract userNoteAddContract) {
        this.userNoteAddContract = userNoteAddContract;
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNoteResponse userNote = this.userNoteAddContract.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userNote);
    }

}
