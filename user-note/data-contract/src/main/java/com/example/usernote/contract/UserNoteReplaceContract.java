package com.example.usernote.contract;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteReplaceContract {

    UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request);

}
