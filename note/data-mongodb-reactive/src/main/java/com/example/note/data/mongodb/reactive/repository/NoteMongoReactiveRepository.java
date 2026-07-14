package com.example.note.data.mongodb.reactive.repository;

import java.util.UUID;

import com.example.note.data.mongodb.reactive.model.NoteReactiveDocument;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteMongoReactiveRepository extends ReactiveMongoRepository<NoteReactiveDocument, UUID> {

}
