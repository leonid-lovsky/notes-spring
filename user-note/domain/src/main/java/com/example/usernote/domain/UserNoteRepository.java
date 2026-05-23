package com.example.usernote.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserNoteRepository {
    UserNote save(UserNote userNote);
    List<UserNote> findByUserId(String userId);
    List<UserNote> findByOwnerId(String ownerId);
    Optional<UserNote> findByUserIdAndNoteId(String userId, UUID noteId);
    void deleteByUserIdAndNoteId(String userId, UUID noteId);
}
