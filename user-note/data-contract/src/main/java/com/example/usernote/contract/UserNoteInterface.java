package com.example.usernote.contract;

import com.example.usernote.domain.UserNoteRequest;

import java.util.UUID;

public interface UserNoteInterface {

    boolean existsByUserNoteId(UUID userNoteId);

    boolean existsByUserId(UUID userId);

    boolean existsByNoteId(UUID noteId);

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);

    Object create(UserNoteRequest request);

    Object findByUserNoteId(UUID userNoteId);

    Object findByUserId(UUID userId);

    Object findByNoteId(UUID noteId);

    Object findByUserIdAndNoteId(UUID userId, UUID noteId);

    Object replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    Object replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    Object mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    Object mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    Object deleteByUserNoteId(UUID userNoteId);

    Object deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
