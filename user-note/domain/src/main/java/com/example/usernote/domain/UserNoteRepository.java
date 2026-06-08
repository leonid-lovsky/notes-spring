package com.example.usernote.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserNoteRepository {

    Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId);

    List<UserNote> findByUserId(UUID userId);

    List<UserNote> findByNoteId(UUID noteId);

    UserNote save(UserNote userNote);

    void deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
