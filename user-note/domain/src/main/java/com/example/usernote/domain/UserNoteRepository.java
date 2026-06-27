package com.example.usernote.domain;

import java.util.*;

public interface UserNoteRepository {

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);

    Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId);

    List<UserNote> findByUserId(UUID userId);

    List<UserNote> findByNoteId(UUID noteId);

    UserNote add(UserNote userNote);

    void replace(UserNote userNote);

    void remove(UUID userId, UUID noteId);
}
