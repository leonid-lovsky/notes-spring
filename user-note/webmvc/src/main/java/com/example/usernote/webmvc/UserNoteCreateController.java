package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNoteAddPort;
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

    private final UserNoteAddPort userNoteAddPort;

    UserNoteCreateController(UserNoteAddPort userNoteAddPort) {
        this.userNoteAddPort = userNoteAddPort;
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNoteResponse userNote = userNoteAddPort.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userNote);
    }

}
