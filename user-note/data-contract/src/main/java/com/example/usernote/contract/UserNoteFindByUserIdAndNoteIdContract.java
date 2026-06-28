package com.example.usernote.contract;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteFindByUserIdAndNoteIdContract {

    Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

}
