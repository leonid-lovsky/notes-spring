package com.example.usernote;

import java.util.UUID;

public interface DeleteUserNoteById {

    UserNoteResponseBody deleteUserNoteById(UUID id);
}
