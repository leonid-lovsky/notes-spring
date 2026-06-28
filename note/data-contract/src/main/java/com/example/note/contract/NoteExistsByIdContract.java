package com.example.note.contract;

import java.util.UUID;

public interface NoteExistsByIdContract {

    boolean existsById(UUID id);

}
