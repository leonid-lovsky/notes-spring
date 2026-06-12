package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteOutputPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteOutputAdapter implements NoteOutputPort {

    private final NoteRepository noteRepository;

    NoteOutputAdapter(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return noteRepository.existsById(id);
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteRepository.findById(id).map(NoteOutputAdapter::toDomain);
    }

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll().stream().map(NoteOutputAdapter::toDomain).toList();
    }

    @Override
    public void add(Note note) {
        noteRepository.save(toEntity(note));
    }

    @Override
    public void replace(Note note) {
        noteRepository.save(toEntity(note));
    }

    @Override
    public void remove(UUID id) {
        noteRepository.deleteById(id);
    }

    private static Note toDomain(NoteEntity entity) {
        return new Note(entity.getId(), entity.getContent());
    }

    private static NoteEntity toEntity(Note note) {
        return new NoteEntity(note.id(), note.content());
    }
}
