package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteFindAllPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

    private final NoteJpaRepository noteJpaRepository;

    NoteFindAllPortAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public List<Note> findAll() {
        return noteJpaRepository.findAll().stream()
                .map(e -> new Note(e.getId(), e.getContent()))
                .toList();
    }
}
