package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService implements FindUserById, CreateUserById, ReplaceUserById, UpdateUserById, DeleteUserById {

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
