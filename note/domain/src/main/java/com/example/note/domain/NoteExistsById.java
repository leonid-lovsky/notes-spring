package com.example.note.domain;

import java.util.UUID;

public interface NoteExistsById {

    boolean existsById(UUID id);
}
