package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveUpdateUserNoteRoleById {

    Mono<UserNoteResponseBody> updateUserNoteRoleById(UUID id, UserNoteRole role);
}
