package com.example.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserServiceReadById {

    UserPayloadResponse readById(@NotNull UUID id);
}
