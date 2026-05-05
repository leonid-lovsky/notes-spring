package com.example.user.contract;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotBlank String username
) {
}
