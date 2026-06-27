package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteAddPort;
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
        UserNote userNote = userNoteAddPort.add(new UserNote(request.userId(), request.noteId(), request.role()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserNoteResponse.from(userNote));
    }
}
