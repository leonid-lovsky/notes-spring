package com.example.usernote.domain;

import java.util.List;
import java.util.UUID;

public interface UserNoteUseCase {

    UserNote create(UUID userId, UUID noteId, UserNoteRole role);

    UserNote findByUserIdAndNoteId(UUID userId, UUID noteId);

    List<UserNote> findByUserId(UUID userId);

    List<UserNote> findByNoteId(UUID noteId);

    UserNote update(UUID userId, UUID noteId, UserNoteRole role);

    void delete(UUID userId, UUID noteId);
}
