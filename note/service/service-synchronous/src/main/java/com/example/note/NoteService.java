package com.example.note;

import java.util.UUID;

public class NoteService implements FindNoteById, CreateNoteById, ReplaceNoteById, UpdateNoteById, DeleteNoteById {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public NoteResponse findNoteById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NoteResponse createNoteById(UUID id, NoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NoteResponse replaceNoteById(UUID id, NoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NoteResponse updateNoteById(UUID id, NoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public NoteResponse deleteNoteById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
