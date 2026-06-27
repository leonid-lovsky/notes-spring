package com.example.usernote.domain;

import java.util.List;
import java.util.UUID;

public interface UserNoteFindByNoteIdPort {

    List<UserNote> findByNoteId(UUID noteId);
}
