package com.example.user;

import java.util.UUID;

public interface FindUserByIdSynchronous {

    UserResponse execute(UUID id);
}
