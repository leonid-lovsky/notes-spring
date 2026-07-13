package com.example.note.contract;

import java.util.List;
import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteServiceInterface extends NoteInterface {

    @Override
    Boolean existsById(UUID id);

    @Override
    NoteResponse add(NoteRequest request);

    @Override
    List<NoteResponse> findAll();

    @Override
    NoteResponse findById(UUID id);

    @Override
    NoteResponse replace(UUID id, NoteRequest request);

    @Override
    NoteResponse merge(UUID id, NoteRequest request);

    @Override
    NoteResponse remove(UUID id);

}
