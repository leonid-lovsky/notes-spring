package com.example.usernote;

import java.util.UUID;

public interface DeleteUserNoteByIdSynchronous {

    UserNoteResponse execute(UUID id);
}
