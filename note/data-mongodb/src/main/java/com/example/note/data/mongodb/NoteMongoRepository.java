package com.example.note.data.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

interface NoteMongoRepository extends MongoRepository<NoteDocument, UUID> {

}
