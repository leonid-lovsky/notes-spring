package com.example.note.data.mongodb;

import com.example.note.domain.Note;
import com.example.note.domain.NoteFindAllPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

    private final NoteMongoRepository noteMongoRepository;

    NoteFindAllPortAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
    }

    @Override
    public List<Note> findAll() {
        return noteMongoRepository.findAll().stream()
                .map(d -> new Note(d.getId(), d.getContent()))
                .toList();
    }
}
