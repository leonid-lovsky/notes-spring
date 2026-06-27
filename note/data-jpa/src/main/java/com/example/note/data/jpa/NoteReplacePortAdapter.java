package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteReplacePort;
import org.springframework.stereotype.Repository;

@Repository
class NoteReplacePortAdapter implements NoteReplacePort {

    private final NoteJpaRepository noteJpaRepository;

    NoteReplacePortAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public void replace(Note note) {
        noteJpaRepository.save(new NoteEntity(note.id(), note.content()));
    }
}
