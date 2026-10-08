package com.example.usernote;

import java.util.Optional;
import java.util.UUID;

public interface UserNoteRepository {

    UserNote save(UserNote userNote);

    Optional<UserNote> findById(UUID id);

    boolean existsById(UUID id);
}
