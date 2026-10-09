package com.example.usernote;

import java.util.UUID;

public interface FindUserNoteById {

    UserNoteResponseBody findUserNoteById(UUID id);
}
