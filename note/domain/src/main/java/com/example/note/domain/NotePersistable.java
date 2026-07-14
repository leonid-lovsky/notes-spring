package com.example.note.domain;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public interface NotePersistable {

    @Nullable UUID getId();

    String getContent();
}
