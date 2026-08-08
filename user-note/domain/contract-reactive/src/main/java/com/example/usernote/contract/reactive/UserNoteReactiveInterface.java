package com.example.usernote.contract.reactive;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;

public interface UserNoteReactiveInterface<B, S, L, V> {

    B existsByUserNoteId(UUID userNoteId);

    B existsByUserId(UUID userId);

    B existsByNoteId(UUID noteId);

    B existsByUserIdAndNoteId(UUID userId, UUID noteId);

    S add(UserNoteRequest request);

    S findByUserNoteId(UUID userNoteId);

    L findByUserId(UUID userId);

    L findByNoteId(UUID noteId);

    S findByUserIdAndNoteId(UUID userId, UUID noteId);

    S replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    S replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    S mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    S mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    V deleteByUserNoteId(UUID userNoteId);

    V deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
