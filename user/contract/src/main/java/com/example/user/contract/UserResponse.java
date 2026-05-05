package com.example.user.contract;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String username
) {
}
