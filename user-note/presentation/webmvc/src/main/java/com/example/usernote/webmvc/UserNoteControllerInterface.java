package com.example.usernote.webmvc;

import java.util.List;

import com.example.usernote.contract.UserNoteInterface;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.ResponseEntity;

public interface UserNoteControllerInterface extends
    UserNoteInterface<ResponseEntity<UserNoteResponse>, ResponseEntity<List<UserNoteResponse>>> {

}
