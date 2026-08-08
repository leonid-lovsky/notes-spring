package com.example.usernote.contract;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import java.util.List;
import java.util.UUID;

public interface UserNoteInterface {

    UserNoteResponse create(UserNoteRequest request);

    UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId);

    UserNoteResponse findByUserNoteId(UUID userNoteId);

    List<UserNoteResponse> findByUserId(UUID userId);

    List<UserNoteResponse> findByNoteId(UUID noteId);

    UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId);

    UserNoteResponse deleteByUserNoteId(UUID userNoteId);
}
