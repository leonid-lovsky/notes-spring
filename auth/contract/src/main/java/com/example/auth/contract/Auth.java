package com.example.auth.contract;

import java.util.UUID;

public record Auth(
    UUID id,
    UUID userId,
    String login,
    String password
) {
}
