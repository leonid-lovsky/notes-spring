package com.example.user;

import java.util.UUID;

public interface ReplaceUserById {

    UserResponse replaceUserById(UUID id, UserRequest request);
}
