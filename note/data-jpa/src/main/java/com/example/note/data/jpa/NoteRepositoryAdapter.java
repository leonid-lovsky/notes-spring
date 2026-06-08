package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteRepositoryAdapter implements NoteRepository {

    private final NoteJpaRepository noteJpaRepository;

    NoteRepositoryAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return noteJpaRepository.existsById(id);
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteJpaRepository.findById(id).map(NoteRepositoryAdapter::toDomain);
    }

    @Override
    public List<Note> findAll() {
        return noteJpaRepository.findAll().stream().map(NoteRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Note save(Note note) {
        return toDomain(noteJpaRepository.save(toEntity(note)));
    }

    @Override
    public void deleteById(UUID id) {
        noteJpaRepository.deleteById(id);
    }

    private static Note toDomain(NoteEntity entity) {
        return new Note(entity.getId(), entity.getContent());
    }

    private static NoteEntity toEntity(Note note) {
        return new NoteEntity(note.getId(), note.getContent());
    }
}
