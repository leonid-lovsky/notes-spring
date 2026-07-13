package com.example.usernote.contract;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import java.util.List;
import java.util.UUID;

public interface UserNoteService {

    UserNoteResponse create(UserNoteRequest request);

    UserNoteResponse findByUserNoteId(UUID id);

    List<UserNoteResponse> findByNoteId(UUID noteId);

    UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId);

    List<UserNoteResponse> findByUserId(UUID userId);

    void remove(UUID userId, UUID noteId);

    UserNoteResponse replaceByUserNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    void deleteByUserNoteId(UUID userNoteId);

    UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId);

    UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId);

    UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId);

    boolean existsByUserNoteId(UUID userNoteId);

    boolean existsByUserId(UUID userId);

    boolean existsByNoteId(UUID noteId);

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);
}
