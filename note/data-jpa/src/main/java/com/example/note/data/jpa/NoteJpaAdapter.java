package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class NoteJpaAdapter implements NoteRepository {

    private final NoteJpaRepository noteJpaRepository;
    private final EntityManager em;

    NoteJpaAdapter(NoteJpaRepository noteJpaRepository, EntityManager em) {
        this.noteJpaRepository = noteJpaRepository;
        this.em = em;
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
        NoteEntity entity = new NoteEntity(note.content());
        em.persist(entity);
        return toDomain(entity);
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
