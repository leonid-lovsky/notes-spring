package com.example.note.data.mongodb;

import com.example.note.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class NoteMongoAdapter implements NoteExistsById, NoteFindById, NoteFindAll, NoteAdd, NoteReplace, NoteRemove {

    private final NoteMongoRepository noteMongoRepository;
    private final MongoTemplate mongoTemplate;

    NoteMongoAdapter(NoteMongoRepository noteMongoRepository, MongoTemplate mongoTemplate) {
        this.noteMongoRepository = noteMongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean existsById(UUID id) {
        return noteMongoRepository.existsById(id);
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteMongoRepository.findById(id).map(NoteMongoAdapter::toDomain);
    }

    @Override
    public List<Note> findAll() {
        return noteMongoRepository.findAll().stream().map(NoteMongoAdapter::toDomain).toList();
    }

    @Override
    public Note add(Note note) {
        UUID id = UUID.randomUUID();
        mongoTemplate.insert(new NoteDocument(id, note.content()));
        return new Note(id, note.content());
    }

    @Override
    public void replace(Note note) {
        mongoTemplate.save(toDocument(note));
    }

    @Override
    public void remove(UUID id) {
        noteMongoRepository.deleteById(id);
    }

    private static Note toDomain(NoteDocument document) {
        return new Note(document.getId(), document.getContent());
    }

    private static NoteDocument toDocument(Note note) {
        return new NoteDocument(note.id(), note.content());
    }
}
