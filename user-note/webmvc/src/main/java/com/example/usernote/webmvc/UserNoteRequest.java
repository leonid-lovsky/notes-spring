package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNoteRole;

import java.util.UUID;

record UserNoteRequest(UUID userId, UUID noteId, UserNoteRole role) {

}
