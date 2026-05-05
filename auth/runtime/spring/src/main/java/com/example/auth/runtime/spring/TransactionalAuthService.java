package com.example.auth.runtime.spring;

import com.example.auth.contract.AuthResponse;
import com.example.auth.contract.AuthService;
import com.example.auth.contract.CreateAuthRequest;
import com.example.auth.contract.ReplaceAuthRequest;
import com.example.auth.contract.UpdateAuthRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class TransactionalAuthService implements AuthService {

    private final AuthService delegate;

    public TransactionalAuthService(AuthService delegate) {
        this.delegate = delegate;
    }

    @Override
    public AuthResponse create(CreateAuthRequest request) {
        return delegate.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthResponse> readAll() {
        return delegate.readAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse readById(UUID id) {
        return delegate.readById(id);
    }

    @Override
    public AuthResponse update(UUID id, UpdateAuthRequest request) {
        return delegate.update(id, request);
    }

    @Override
    public AuthResponse replace(UUID id, ReplaceAuthRequest request) {
        return delegate.replace(id, request);
    }

    @Override
    public AuthResponse delete(UUID id) {
        return delegate.delete(id);
    }
}
