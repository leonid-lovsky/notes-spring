package com.example.usernote.data.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

interface UserNoteMongoRepository extends MongoRepository<UserNoteDocument, UserNoteKey> {

    List<UserNoteDocument> findByIdUserId(UUID userId);

    List<UserNoteDocument> findByIdNoteId(UUID noteId);

}
