package com.example.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserPayloadResponse create(@Valid @NotNull UserPayloadRequest userPayloadRequest);

    List<UserPayloadResponse> read();

    UserPayloadResponse readById(@NotNull UUID id);

    UserPayloadResponse updateById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest);

    UserPayloadResponse replaceById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest);

    UserPayloadResponse deleteById(@NotNull UUID id);
}
