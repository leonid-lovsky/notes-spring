package com.example.user;

import java.util.UUID;

public interface DeleteUserByIdSynchronous {

    UserResponse execute(UUID id);
}
