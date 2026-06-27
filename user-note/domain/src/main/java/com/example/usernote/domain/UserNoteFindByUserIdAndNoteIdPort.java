package com.example.usernote.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserNoteFindByUserIdAndNoteIdPort {

    Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);
}
