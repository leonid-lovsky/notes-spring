package com.example.usernote.data.mongodb.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.data.mongodb.model.UserNoteDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteMongoRepository extends MongoRepository<UserNoteDocument, UUID> {

    List<UserNoteDocument> findByUserId(UUID userId);

    List<UserNoteDocument> findByNoteId(UUID noteId);

    Optional<UserNoteDocument> findByUserIdAndNoteId(UUID userId, UUID noteId);

    boolean existsByUserId(UUID userId);

    boolean existsByNoteId(UUID noteId);

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);
}
