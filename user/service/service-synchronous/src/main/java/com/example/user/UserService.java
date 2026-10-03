package com.example.user;

import java.util.UUID;

public class UserService implements FindUserById, CreateUserById, ReplaceUserById, UpdateUserById, DeleteUserById {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse findUserById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse createUserById(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse replaceUserById(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse updateUserById(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse deleteUserById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
