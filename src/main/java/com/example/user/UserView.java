package com.example.user;

import java.time.Instant;
import java.util.UUID;

public record UserView(
    UUID id,
    String username,
    String displayName,
    Instant createdAt
) {

}
