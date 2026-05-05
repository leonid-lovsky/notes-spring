package com.example.usernote.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserNoteStore {

    UserNote create(UserNote userNote);

    List<UserNote> readAll();

    Optional<UserNote> readById(UUID id);

    UserNote update(UserNote userNote);

    UserNote replace(UserNote userNote);

    void deleteById(UUID id);
}
