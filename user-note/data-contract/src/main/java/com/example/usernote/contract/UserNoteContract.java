package com.example.usernote.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteContract {

    UserNoteResponse add(UserNoteRequest request);

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);

    Optional<UserNoteResponse> findById(UUID id);

    List<UserNoteResponse> findByNoteId(UUID noteId);

    Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

    List<UserNoteResponse> findByUserId(UUID userId);

    void remove(UUID userId, UUID noteId);

    UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request);

}
