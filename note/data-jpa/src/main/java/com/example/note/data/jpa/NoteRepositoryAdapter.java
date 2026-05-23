package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteRepositoryAdapter implements NoteRepository {

    private final NoteJpaRepository jpa;

    NoteRepositoryAdapter(NoteJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Note save(Note note) {
        return toNote(jpa.save(toEntity(note)));
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return jpa.findById(id).map(this::toNote);
    }

    @Override
    public List<Note> findByOwnerId(String ownerId) {
        return jpa.findByOwnerId(ownerId).stream().map(this::toNote).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    private NoteEntity toEntity(Note n) {
        var e = new NoteEntity();
        e.id = n.id();
        e.ownerId = n.ownerId();
        e.title = n.title();
        e.content = n.content();
        e.createdAt = n.createdAt();
        return e;
    }

    private Note toNote(NoteEntity e) {
        return new Note(e.id, e.ownerId, e.title, e.content, e.createdAt);
    }
}
