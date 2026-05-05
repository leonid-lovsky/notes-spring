package com.example.user.contract;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
    @NotBlank String username
) {
}
