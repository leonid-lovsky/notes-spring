package com.example.usernote;

import java.util.UUID;

public interface FindUserNoteById {

    UserNoteResponse findUserNoteById(UUID id);
}
