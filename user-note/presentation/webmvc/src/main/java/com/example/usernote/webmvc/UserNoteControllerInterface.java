package com.example.usernote.webmvc;

import com.example.usernote.contract.UserNoteInterface;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserNoteControllerInterface extends UserNoteInterface {

    @Override
    ResponseEntity<UserNoteResponse> create(UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    ResponseEntity<UserNoteResponse> findByUserNoteId(UUID userNoteId);

    @Override
    ResponseEntity<List<UserNoteResponse>> findByUserId(UUID userId);

    @Override
    ResponseEntity<List<UserNoteResponse>> findByNoteId(UUID noteId);

    @Override
    ResponseEntity<UserNoteResponse> replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    ResponseEntity<UserNoteResponse> deleteByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    ResponseEntity<UserNoteResponse> deleteByUserNoteId(UUID userNoteId);
}
