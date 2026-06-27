package com.example.note.domain;

import java.util.UUID;

public interface NoteExistsByIdPort {

    boolean existsById(UUID id);

}
