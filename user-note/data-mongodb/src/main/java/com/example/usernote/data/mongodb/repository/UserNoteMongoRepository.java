package com.example.usernote.data.mongodb.repository;

import java.util.List;
import java.util.UUID;

import com.example.usernote.data.mongodb.document.UserNoteDocument;
import com.example.usernote.data.mongodb.document.UserNoteKey;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserNoteMongoRepository extends MongoRepository<UserNoteDocument, UserNoteKey> {

    List<UserNoteDocument> findByIdUserId(UUID userId);

    List<UserNoteDocument> findByIdNoteId(UUID noteId);

}
