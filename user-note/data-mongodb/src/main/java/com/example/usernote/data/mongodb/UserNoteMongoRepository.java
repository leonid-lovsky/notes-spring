package com.example.usernote.data.mongodb;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

interface UserNoteMongoRepository extends MongoRepository<UserNoteDocument, UserNoteKey> {

    List<UserNoteDocument> findByIdUserId(UUID userId);

    List<UserNoteDocument> findByIdNoteId(UUID noteId);

}
