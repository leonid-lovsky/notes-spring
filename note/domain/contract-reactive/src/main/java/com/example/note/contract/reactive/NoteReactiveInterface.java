package com.example.note.contract.reactive;

import java.util.UUID;

import com.example.note.domain.NoteRequest;

public interface NoteReactiveInterface<B, S, L, V> {

    B existsById(UUID id);

    S add(NoteRequest request);

    L findAll();

    S findById(UUID id);

    S replace(UUID id, NoteRequest request);

    S merge(UUID id, NoteRequest request);

    V remove(UUID id);
}
