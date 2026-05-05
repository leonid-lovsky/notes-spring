package com.example.auth.contract;

import java.util.UUID;

public record AuthResponse(
    UUID id,
    UUID userId,
    String login
) {
}
