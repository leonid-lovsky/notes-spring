package com.example.usernote.contract;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteAddContract {

    UserNoteResponse add(UserNoteRequest request);

}
