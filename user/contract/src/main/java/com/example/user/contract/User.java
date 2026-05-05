package com.example.user.contract;

import java.util.UUID;

public record User(
    UUID id,
    String username
) {
}
