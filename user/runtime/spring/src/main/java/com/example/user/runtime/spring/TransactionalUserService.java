package com.example.user.runtime.spring;

import com.example.user.contract.CreateUserRequest;
import com.example.user.contract.ReplaceUserRequest;
import com.example.user.contract.UpdateUserRequest;
import com.example.user.contract.UserResponse;
import com.example.user.contract.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
public class TransactionalUserService implements UserService {

    private final UserService delegate;

    public TransactionalUserService(UserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        return delegate.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> readAll() {
        return delegate.readAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse readById(UUID id) {
        return delegate.readById(id);
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest request) {
        return delegate.update(id, request);
    }

    @Override
    public UserResponse replace(UUID id, ReplaceUserRequest request) {
        return delegate.replace(id, request);
    }

    @Override
    public UserResponse delete(UUID id) {
        return delegate.delete(id);
    }
}
