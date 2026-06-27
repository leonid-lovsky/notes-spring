package com.example.user.domain;

import java.util.UUID;

public interface UserExistsById {

    boolean existsById(UUID id);
}
