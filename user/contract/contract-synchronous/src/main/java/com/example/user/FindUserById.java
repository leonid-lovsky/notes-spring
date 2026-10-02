package com.example.user;

import java.util.UUID;

public interface FindUserById {

    UserResponse findUserById(UUID id);
}
