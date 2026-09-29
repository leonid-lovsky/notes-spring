package com.example.usernote;

import java.util.UUID;

public interface FindUserNoteByIdSynchronous {

    UserNoteResponse execute(UUID id);
}
