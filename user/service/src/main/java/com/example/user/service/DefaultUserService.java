package com.example.user.service;

import com.example.user.contract.CreateUserRequest;
import com.example.user.contract.ReplaceUserRequest;
import com.example.user.contract.UpdateUserRequest;
import com.example.user.contract.User;
import com.example.user.contract.UserNotFoundException;
import com.example.user.contract.UserResponse;
import com.example.user.contract.UserService;
import com.example.user.contract.UserStore;

import java.util.List;
import java.util.UUID;

public class DefaultUserService implements UserService {

    private final UserStore userStore;
    private final UserServiceMapper mapper;

    public DefaultUserService(UserStore userStore, UserServiceMapper mapper) {
        this.userStore = userStore;
        this.mapper = mapper;
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        return mapper.toResponse(userStore.create(mapper.toNewUser(request)));
    }

    @Override
    public List<UserResponse> readAll() {
        return userStore.readAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public UserResponse readById(UUID id) {
        return mapper.toResponse(getRequired(id));
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest request) {
        getRequired(id);
        return mapper.toResponse(userStore.update(mapper.toUpdatedUser(id, request)));
    }

    @Override
    public UserResponse replace(UUID id, ReplaceUserRequest request) {
        getRequired(id);
        return mapper.toResponse(userStore.replace(mapper.toReplacedUser(id, request)));
    }

    @Override
    public UserResponse delete(UUID id) {
        User user = getRequired(id);
        userStore.deleteById(id);
        return mapper.toResponse(user);
    }

    private User getRequired(UUID id) {
        return userStore.readById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
