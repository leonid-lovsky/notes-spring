package com.example.user;

import java.util.UUID;

public interface ReplaceUserByIdSynchronous {

    UserResponse execute(UUID id, UserRequest request);
}
