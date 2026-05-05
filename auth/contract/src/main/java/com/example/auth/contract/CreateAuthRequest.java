package com.example.auth.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAuthRequest(
    @NotNull UUID userId,
    @NotBlank String login,
    @NotBlank String password
) {
}
