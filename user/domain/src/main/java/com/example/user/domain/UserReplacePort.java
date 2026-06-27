package com.example.user.domain;

import java.util.UUID;

public interface UserReplacePort {

    UserResponse replace(UUID id, UserRequest request);
}
