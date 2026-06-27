package com.example.note.data.mongodb;

import com.example.note.domain.Note;
import com.example.note.domain.NoteFindByIdPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class NoteFindByIdPortAdapter implements NoteFindByIdPort {

    private final NoteMongoRepository noteMongoRepository;

    NoteFindByIdPortAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return noteMongoRepository.findById(id)
                .map(d -> new Note(d.getId(), d.getContent()));
    }
}
