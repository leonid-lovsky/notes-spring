package com.example.note.data.mongodb.repository;

import java.util.UUID;

import com.example.note.data.mongodb.model.NoteDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteMongoRepository extends MongoRepository<NoteDocument, UUID> {

}
