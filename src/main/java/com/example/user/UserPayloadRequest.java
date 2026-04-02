package com.example.user;

import jakarta.validation.constraints.NotNull;

public record UserPayloadRequest(
    @NotNull String content
) {

}
