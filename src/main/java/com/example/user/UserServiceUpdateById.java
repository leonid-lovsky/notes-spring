package com.example.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserServiceUpdateById {

    UserPayloadResponse updateById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest);
}
