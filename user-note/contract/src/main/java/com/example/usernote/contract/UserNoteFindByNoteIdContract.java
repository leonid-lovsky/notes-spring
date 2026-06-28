package com.example.usernote.contract;

import java.util.List;
import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteFindByNoteIdContract {

    List<UserNoteResponse> findByNoteId(UUID noteId);

}
