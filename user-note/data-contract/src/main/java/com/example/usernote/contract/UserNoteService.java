package com.example.usernote.contract;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import java.util.List;
import java.util.UUID;

public interface UserNoteService extends UserNoteInterface {

    UserNoteResponse create(UserNoteRequest request);

    UserNoteResponse findByUserNoteId(UUID userNoteId);

    List<UserNoteResponse> findByUserId(UUID userId);

    List<UserNoteResponse> findByNoteId(UUID noteId);

    UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId);

    UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    UserNoteResponse deleteByUserNoteId(UUID userNoteId);

    UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
