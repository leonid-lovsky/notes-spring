package com.example.usernote.domain;

import java.util.*;

public interface UserNoteFindByUserIdAndNoteId {

    Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId);
}
