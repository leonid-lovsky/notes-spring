package com.example.usernote.domain;

import java.util.UUID;

public interface UserNoteExistsByUserIdAndNoteId {

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);
}
