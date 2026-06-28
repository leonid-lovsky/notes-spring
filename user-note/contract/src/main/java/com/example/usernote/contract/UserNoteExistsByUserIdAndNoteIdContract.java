package com.example.usernote.contract;

import java.util.UUID;

public interface UserNoteExistsByUserIdAndNoteIdContract {

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);

}
