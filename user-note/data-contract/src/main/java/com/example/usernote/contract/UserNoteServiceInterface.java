package com.example.usernote.contract;

import java.util.List;
import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteServiceInterface extends UserNoteInterface {

    @Override
    Boolean existsByUserNoteId(UUID userNoteId);

    @Override
    Boolean existsByUserId(UUID userId);

    @Override
    Boolean existsByNoteId(UUID noteId);

    @Override
    Boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    UserNoteResponse create(UserNoteRequest request);

    @Override
    UserNoteResponse findByUserNoteId(UUID userNoteId);

    @Override
    List<UserNoteResponse> findByUserId(UUID userId);

    @Override
    List<UserNoteResponse> findByNoteId(UUID noteId);

    @Override
    UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    UserNoteResponse deleteByUserNoteId(UUID userNoteId);

    @Override
    UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId);

}
