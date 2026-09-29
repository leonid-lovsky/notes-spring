package com.example.user;

import java.util.UUID;

public interface CreateUserByIdSynchronous {

    UserResponse execute(UUID id, UserRequest request);
}
