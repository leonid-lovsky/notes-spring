package com.example.user;

import java.util.UUID;

public interface UpdateUserById {

    UserResponse updateUserById(UUID id, UserRequest request);
}
