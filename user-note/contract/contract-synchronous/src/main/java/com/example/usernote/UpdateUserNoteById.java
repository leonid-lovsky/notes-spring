package com.example.usernote;

import java.util.UUID;

public interface UpdateUserNoteById {

    UserNoteResponse updateUserNoteById(UUID id, UserNoteRequest request);
}
