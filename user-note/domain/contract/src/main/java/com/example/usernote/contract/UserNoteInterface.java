package com.example.usernote.contract;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;

public interface UserNoteInterface<S, L> {

    S create(UserNoteRequest request);

    S findByUserIdAndNoteId(UUID userId, UUID noteId);

    S findByUserNoteId(UUID userNoteId);

    L findByUserId(UUID userId);

    L findByNoteId(UUID noteId);

    S replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    S replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    S mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    S mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    S deleteByUserIdAndNoteId(UUID userId, UUID noteId);

    S deleteByUserNoteId(UUID userNoteId);
}
