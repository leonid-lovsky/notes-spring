package com.example.note.data.mongodb;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

interface NoteMongoRepository extends MongoRepository<NoteDocument, UUID> {

}
