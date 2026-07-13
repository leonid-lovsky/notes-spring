package com.example.note.contract.reactive;

import java.util.UUID;

import com.example.note.domain.NoteRequest;

public interface NoteInterfaceReactive {

    Object existsById(UUID id);

    Object add(NoteRequest request);

    Object findAll();

    Object findById(UUID id);

    Object replace(UUID id, NoteRequest request);

    Object merge(UUID id, NoteRequest request);

    Object remove(UUID id);
}
