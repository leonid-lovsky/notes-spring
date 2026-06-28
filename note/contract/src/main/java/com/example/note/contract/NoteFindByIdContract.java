package com.example.note.contract;

import java.util.Optional;
import java.util.UUID;

import com.example.note.domain.NoteResponse;

public interface NoteFindByIdContract {

    Optional<NoteResponse> findById(UUID id);

}
