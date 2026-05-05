package com.example.note.runtime.spring;

import com.example.note.contract.CreateNoteRequest;
import com.example.note.contract.NoteResponse;
import com.example.note.contract.NoteService;
import com.example.note.contract.ReplaceNoteRequest;
import com.example.note.contract.UpdateNoteRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class TransactionalNoteService implements NoteService {

    private final NoteService delegate;

    public TransactionalNoteService(NoteService delegate) {
        this.delegate = delegate;
    }

    @Override
    public NoteResponse create(CreateNoteRequest request) {
        return delegate.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> readAll() {
        return delegate.readAll();
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse readById(UUID id) {
        return delegate.readById(id);
    }

    @Override
    public NoteResponse update(UUID id, UpdateNoteRequest request) {
        return delegate.update(id, request);
    }

    @Override
    public NoteResponse replace(UUID id, ReplaceNoteRequest request) {
        return delegate.replace(id, request);
    }

    @Override
    public NoteResponse delete(UUID id) {
        return delegate.delete(id);
    }
}
