package com.example.note.data.jpa;

import com.example.note.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class NoteJpaAdapter implements NoteExistsById, NoteFindById, NoteFindAll, NoteAdd, NoteReplace, NoteRemove {

    private final NoteJpaRepository noteJpaRepository;

    NoteJpaAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return noteJpaRepository.existsById(id);
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteJpaRepository.findById(id).map(NoteJpaAdapter::toDomain);
    }

    @Override
    public List<Note> findAll() {
        return noteJpaRepository.findAll().stream().map(NoteJpaAdapter::toDomain).toList();
    }

    @Override
    public Note add(Note note) {
        NoteEntity saved = noteJpaRepository.save(new NoteEntity(note.content()));
        return toDomain(saved);
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
