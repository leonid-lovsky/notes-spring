package com.example.user;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String displayName
) {

}
