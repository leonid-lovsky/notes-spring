package com.example.usernote.domain;

import java.util.Optional;
import java.util.UUID;

public interface NoteClient {

    Optional<NoteSummary> findById(UUID noteId);

    record NoteSummary(UUID id, String ownerId, String title) {}
}
