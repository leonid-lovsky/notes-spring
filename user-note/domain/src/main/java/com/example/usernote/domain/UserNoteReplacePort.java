package com.example.usernote.domain;

import java.util.UUID;

public interface UserNoteReplacePort {

    UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request);
}
