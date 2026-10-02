package com.example.user;

import java.util.UUID;

public interface CreateUserById {

    UserResponse createUserById(UUID id, UserRequest request);
}
