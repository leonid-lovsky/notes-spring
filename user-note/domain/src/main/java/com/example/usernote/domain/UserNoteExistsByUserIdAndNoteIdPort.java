package com.example.usernote.domain;

import java.util.UUID;

public interface UserNoteExistsByUserIdAndNoteIdPort {

	boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);

}
