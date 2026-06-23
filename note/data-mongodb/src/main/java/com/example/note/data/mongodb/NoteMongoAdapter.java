package com.example.note.data.mongodb;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteMongoAdapter implements NoteRepository {

    private final NoteMongoRepository noteMongoRepository;

    NoteMongoAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
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
    public void add(Note note) {
        noteMongoRepository.save(toDocument(note));
    }

    @Override
    public void replace(Note note) {
        noteMongoRepository.save(toDocument(note));
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
