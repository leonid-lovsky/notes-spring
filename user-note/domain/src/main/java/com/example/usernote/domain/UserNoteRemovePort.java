package com.example.usernote.domain;

import java.util.UUID;

public interface UserNoteRemovePort {

    void remove(UUID userId, UUID noteId);
}
