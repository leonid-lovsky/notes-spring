package com.example.note.contract;

import java.util.UUID;

import com.example.note.domain.NoteRequest;

public interface NoteInterface<B, S, L> {

    B existsById(UUID id);

    S add(NoteRequest request);

    L findAll();

    S findById(UUID id);

    S replace(UUID id, NoteRequest request);

    S merge(UUID id, NoteRequest request);

    S remove(UUID id);
}
