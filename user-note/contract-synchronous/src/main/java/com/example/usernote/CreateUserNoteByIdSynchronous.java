package com.example.usernote;

import java.util.UUID;

public interface CreateUserNoteByIdSynchronous {

    UserNoteResponse execute(UUID id, UserNoteRequest request);
}
