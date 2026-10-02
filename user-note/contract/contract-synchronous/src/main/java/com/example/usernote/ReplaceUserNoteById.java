package com.example.usernote;

import java.util.UUID;

public interface ReplaceUserNoteById {

    UserNoteResponse replaceUserNoteById(UUID id, UserNoteRequest request);
}
