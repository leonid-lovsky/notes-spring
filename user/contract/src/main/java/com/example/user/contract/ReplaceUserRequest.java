package com.example.user.contract;

import jakarta.validation.constraints.NotBlank;

public record ReplaceUserRequest(
    @NotBlank String username
) {
}
