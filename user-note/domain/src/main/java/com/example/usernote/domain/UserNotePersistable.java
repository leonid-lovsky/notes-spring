package com.example.usernote.domain;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public interface UserNotePersistable {

    @Nullable UUID getId();

    UUID getUserId();

    UUID getNoteId();

    UserNoteRole getRole();
}
