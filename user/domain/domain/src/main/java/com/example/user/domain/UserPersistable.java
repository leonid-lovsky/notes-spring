package com.example.user.domain;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public interface UserPersistable {

    @Nullable UUID getId();

    String getUsername();

    String getEmail();
}
