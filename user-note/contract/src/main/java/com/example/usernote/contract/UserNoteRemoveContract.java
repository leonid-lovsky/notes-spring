package com.example.usernote.contract;

import java.util.UUID;

public interface UserNoteRemoveContract {

    void remove(UUID userId, UUID noteId);

}
