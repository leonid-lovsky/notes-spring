package com.example.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserPayloadResponse(
    @NotNull UUID id,
    @NotNull String content
) {

}
