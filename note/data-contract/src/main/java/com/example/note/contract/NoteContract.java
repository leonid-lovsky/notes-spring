package com.example.note.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteContract {

    NoteResponse add(NoteRequest request);

    boolean existsById(UUID id);

    List<NoteResponse> findAll();

    Optional<NoteResponse> findById(UUID id);

    void remove(UUID id);

    NoteResponse replace(UUID id, NoteRequest request);

}
