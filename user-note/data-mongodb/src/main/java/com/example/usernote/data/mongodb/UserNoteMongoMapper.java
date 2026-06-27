package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

interface UserNoteMongoMapper {

    UserNoteDocument toDocument(UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteDocument document);
}
