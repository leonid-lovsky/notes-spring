package com.example.usernote;

import java.util.UUID;

public interface DeleteUserNoteById {

    UserNoteResponse deleteUserNoteById(UUID id);
}
