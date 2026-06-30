package com.example.usernote.data.mongodb.mapper;

import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteMongoMapperContract {

    UserNoteDocument toDocument(UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteDocument document);

}
