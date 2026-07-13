package com.example.usernote.data.mongodb.reactive.mapper;

import java.util.UUID;

import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteMongoReactiveMapperContract {

    UserNoteReactiveDocument toNewDocument(UserNoteRequest request);

    UserNoteReactiveDocument toExistingDocument(UUID id, UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteReactiveDocument document);
}
