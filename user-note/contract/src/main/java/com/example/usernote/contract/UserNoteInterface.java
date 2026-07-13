package com.example.usernote.contract;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;

public interface UserNoteInterface {

    Object existsByUserNoteId(UUID userNoteId);

    Object existsByUserId(UUID userId);

    Object existsByNoteId(UUID noteId);

    Object existsByUserIdAndNoteId(UUID userId, UUID noteId);

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
