package com.example.usernote;

import java.util.UUID;

public interface UpdateUserNoteByIdSynchronous {

    UserNoteResponse execute(UUID id, UserNoteRequest request);
}
