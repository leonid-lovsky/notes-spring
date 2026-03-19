package com.example.user;

import java.util.UUID;

public record AuthenticatedUser(
    UUID id,
    String username
) {

}
