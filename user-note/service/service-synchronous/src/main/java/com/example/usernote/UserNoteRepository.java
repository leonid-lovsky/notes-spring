package com.example.usernote;

import java.util.Optional;
import java.util.UUID;

public interface UserNoteRepository {

    Optional<UserNote> findById(UUID id);

    boolean existsById(UUID id);

    UserNote save(UserNote userNote);
}
