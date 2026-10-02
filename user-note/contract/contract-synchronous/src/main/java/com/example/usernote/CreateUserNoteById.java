package com.example.usernote;

import java.util.UUID;

public interface CreateUserNoteById {

    UserNoteResponse createUserNoteById(UUID id, UserNoteRequest request);
}
