package com.example.auth.service;

import com.example.auth.contract.Auth;
import com.example.auth.contract.AuthNotFoundException;
import com.example.auth.contract.AuthResponse;
import com.example.auth.contract.AuthService;
import com.example.auth.contract.AuthStore;
import com.example.auth.contract.CreateAuthRequest;
import com.example.auth.contract.ReplaceAuthRequest;
import com.example.auth.contract.UpdateAuthRequest;
import com.example.user.contract.UserService;

import java.util.List;
import java.util.UUID;

public class DefaultAuthService implements AuthService {

    private final AuthStore authStore;
    private final UserService userService;
    private final AuthServiceMapper mapper;

    public DefaultAuthService(AuthStore authStore, UserService userService, AuthServiceMapper mapper) {
        this.authStore = authStore;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public AuthResponse create(CreateAuthRequest request) {
        requireUser(request.userId());
        return mapper.toResponse(authStore.create(mapper.toNewAuth(request)));
    }

    @Override
    public List<AuthResponse> readAll() {
        return authStore.readAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public AuthResponse readById(UUID id) {
        return mapper.toResponse(getRequired(id));
    }

    @Override
    public AuthResponse update(UUID id, UpdateAuthRequest request) {
        getRequired(id);
        requireUser(request.userId());
        return mapper.toResponse(authStore.update(mapper.toUpdatedAuth(id, request)));
    }

    @Override
    public AuthResponse replace(UUID id, ReplaceAuthRequest request) {
        getRequired(id);
        requireUser(request.userId());
        return mapper.toResponse(authStore.replace(mapper.toReplacedAuth(id, request)));
    }

    @Override
    public AuthResponse delete(UUID id) {
        Auth auth = getRequired(id);
        authStore.deleteById(id);
        return mapper.toResponse(auth);
    }

    private Auth getRequired(UUID id) {
        return authStore.readById(id).orElseThrow(() -> new AuthNotFoundException(id));
    }

    private void requireUser(UUID userId) {
        userService.readById(userId);
    }
}
