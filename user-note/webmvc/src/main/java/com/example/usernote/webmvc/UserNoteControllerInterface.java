package com.example.usernote.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteInterface;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.ResponseEntity;

public interface UserNoteControllerInterface extends UserNoteInterface {

    @Override
    ResponseEntity<Boolean> existsByUserNoteId(UUID userNoteId);

    @Override
    ResponseEntity<Boolean> existsByUserId(UUID userId);

    @Override
    ResponseEntity<Boolean> existsByNoteId(UUID noteId);

    @Override
    ResponseEntity<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    ResponseEntity<UserNoteResponse> create(UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> findByUserNoteId(UUID userNoteId);

    @Override
    ResponseEntity<List<UserNoteResponse>> findByUserId(UUID userId);

    @Override
    ResponseEntity<List<UserNoteResponse>> findByNoteId(UUID noteId);

    @Override
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    ResponseEntity<UserNoteResponse> replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> deleteByUserNoteId(UUID userNoteId);

    @Override
    ResponseEntity<UserNoteResponse> deleteByUserIdAndNoteId(UUID userId, UUID noteId);

}
