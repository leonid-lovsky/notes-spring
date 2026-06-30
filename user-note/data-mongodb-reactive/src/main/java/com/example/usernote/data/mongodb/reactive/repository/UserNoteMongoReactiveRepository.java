package com.example.usernote.data.mongodb.reactive.repository;

import java.util.UUID;

import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveKey;
import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserNoteMongoReactiveRepository
        extends ReactiveMongoRepository<UserNoteReactiveDocument, UserNoteReactiveKey> {

    Flux<UserNoteReactiveDocument> findByIdUserId(UUID userId);

    Flux<UserNoteReactiveDocument> findByIdNoteId(UUID noteId);

}
