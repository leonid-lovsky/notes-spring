package com.example.usernote.domain;

import java.util.*;

public interface UserNoteFindByNoteId {

    List<UserNote> findByNoteId(UUID noteId);
}
