package com.example.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserServiceReplaceById {

    UserPayloadResponse replaceById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest);
}
