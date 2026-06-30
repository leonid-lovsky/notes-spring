package com.example.usernote.contract;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteFindByIdContract {

    Optional<UserNoteResponse> findById(UUID id);

}
