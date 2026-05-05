package com.example.usernote.runtime.spring;

import com.example.usernote.contract.CreateUserNoteRequest;
import com.example.usernote.contract.ReplaceUserNoteRequest;
import com.example.usernote.contract.UpdateUserNoteRequest;
import com.example.usernote.contract.UserNoteResponse;
import com.example.usernote.contract.UserNoteService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class TransactionalUserNoteService implements UserNoteService {

    private final UserNoteService delegate;

    public TransactionalUserNoteService(UserNoteService delegate) {
        this.delegate = delegate;
    }

    @Override
    public UserNoteResponse create(CreateUserNoteRequest request) {
        return delegate.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNoteResponse> readAll() {
        return delegate.readAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserNoteResponse readById(UUID id) {
        return delegate.readById(id);
    }

    @Override
    public UserNoteResponse update(UUID id, UpdateUserNoteRequest request) {
        return delegate.update(id, request);
    }

    @Override
    public UserNoteResponse replace(UUID id, ReplaceUserNoteRequest request) {
        return delegate.replace(id, request);
    }

    @Override
    public UserNoteResponse delete(UUID id) {
        return delegate.delete(id);
    }
}
