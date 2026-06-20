package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteOutputAdapter implements NoteRepository {

    private final NoteJpaRepository noteJpaRepository;

    NoteOutputAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return noteJpaRepository.existsById(id);
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteJpaRepository.findById(id).map(NoteOutputAdapter::toDomain);
    }

    @Override
    public List<Note> findAll() {
        return noteJpaRepository.findAll().stream().map(NoteOutputAdapter::toDomain).toList();
    }

    @Override
    public void add(Note note) {
        noteJpaRepository.save(toEntity(note));
    }

    @Override
    public void replace(Note note) {
        noteJpaRepository.save(toEntity(note));
    }

    @Override
    public void remove(UUID id) {
        noteJpaRepository.deleteById(id);
    }

    private static Note toDomain(NoteEntity entity) {
        return new Note(entity.getId(), entity.getContent());
    }

    private static NoteEntity toEntity(Note note) {
        return new NoteEntity(note.id(), note.content());
    }
}
