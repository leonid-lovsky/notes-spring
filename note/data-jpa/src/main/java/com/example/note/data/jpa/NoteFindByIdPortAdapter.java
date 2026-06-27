package com.example.note.data.jpa;

import com.example.note.domain.Note;
import com.example.note.domain.NoteFindByIdPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class NoteFindByIdPortAdapter implements NoteFindByIdPort {

    private final NoteJpaRepository noteJpaRepository;

    NoteFindByIdPortAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteJpaRepository.findById(id)
                .map(e -> new Note(e.getId(), e.getContent()));
    }
}
