package com.example.note.domain;

import java.util.Optional;
import java.util.UUID;

public interface NoteFindByIdPort {

    Optional<Note> findById(UUID id);
}
