package com.example.user;

import java.util.UUID;

public interface UpdateUserByIdSynchronous {

    UserResponse execute(UUID id, UserRequest request);
}
