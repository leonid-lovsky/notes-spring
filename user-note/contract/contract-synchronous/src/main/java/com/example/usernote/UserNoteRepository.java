package com.example.usernote;

import java.util.UUID;

public interface UserNoteRepository {

    UserNoteResponseBody findUserNoteById(UUID id);

    UserNoteResponseBody createUserNote(UserNoteRequestBody requestBody);

    UserNoteResponseBody updateUserNoteRoleById(UUID id, UserNoteRole role);

    UserNoteResponseBody deleteUserNoteById(UUID id);
}
