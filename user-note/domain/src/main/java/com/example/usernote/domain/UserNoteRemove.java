package com.example.usernote.domain;

import java.util.UUID;

public interface UserNoteRemove {

    void remove(UUID userId, UUID noteId);
}
