package com.example.usernote.data.mongodb.reactive.mapper;

import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteReactiveDocumentMapperContract {

    UserNoteReactiveDocument toDocument(UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteReactiveDocument document);

}
