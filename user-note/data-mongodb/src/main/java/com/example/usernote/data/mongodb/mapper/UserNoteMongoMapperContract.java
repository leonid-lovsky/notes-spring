package com.example.usernote.data.mongodb.mapper;

import java.util.UUID;

import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteMongoMapperContract {

    UserNoteDocument toNewDocument(UserNoteRequest request);

    UserNoteDocument toExistingDocument(UUID id, UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteDocument document);

}
