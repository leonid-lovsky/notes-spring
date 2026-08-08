package com.example.usernote.contract;

import java.util.List;
import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteServiceInterface extends UserNoteInterface<UserNoteResponse, List<UserNoteResponse>> {

    Boolean existsByUserNoteId(UUID userNoteId);

    Boolean existsByUserId(UUID userId);

    Boolean existsByNoteId(UUID noteId);

    Boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);
}
