package com.example.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserServiceDeleteById {

    UserPayloadResponse deleteById(@NotNull UUID id);
}
