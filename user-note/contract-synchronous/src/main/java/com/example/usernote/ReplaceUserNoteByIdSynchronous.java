package com.example.usernote;

import java.util.UUID;

public interface ReplaceUserNoteByIdSynchronous {

    UserNoteResponse execute(UUID id, UserNoteRequest request);
}
