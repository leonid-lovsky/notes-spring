package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteAddPort;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddPortAdapter implements NoteAddPort {

    private final NoteJpaRepository noteJpaRepository;

    NoteAddPortAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public Note add(Note note) {
        NoteEntity saved = noteJpaRepository.save(new NoteEntity(note.content()));
        return new Note(saved.getId(), saved.getContent());
    }
}
