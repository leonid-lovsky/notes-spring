package com.example.application.user;

import jakarta.validation.constraints.NotNull;

public record UserRequest(
    @NotNull String username
) {

}
