package com.example.note;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Service;

@Service
public class NoteService implements FindNoteById, CreateNoteById, ReplaceNoteById, UpdateNoteById, DeleteNoteById {

    private final CrudRepository<Note, UUID> noteCrudRepository;
    private final PagingAndSortingRepository<Note, UUID> noteSortingRepository;
    private final QueryByExampleExecutor<Note> noteQueryByExampleExecutor;

    public NoteService(
        CrudRepository<Note, UUID> noteCrudRepository,
        PagingAndSortingRepository<Note, UUID> noteSortingRepository,
        QueryByExampleExecutor<Note> noteQueryByExampleExecutor
    ) {
        this.noteCrudRepository = noteCrudRepository;
        this.noteSortingRepository = noteSortingRepository;
        this.noteQueryByExampleExecutor = noteQueryByExampleExecutor;
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
