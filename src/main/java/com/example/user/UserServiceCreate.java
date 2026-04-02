package com.example.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UserServiceCreate {

    UserPayloadResponse create(@Valid @NotNull UserPayloadRequest userPayloadRequest);
}
