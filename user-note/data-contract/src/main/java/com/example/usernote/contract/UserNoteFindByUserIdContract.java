package com.example.usernote.contract;

import java.util.List;
import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteFindByUserIdContract {

    List<UserNoteResponse> findByUserId(UUID userId);

}
