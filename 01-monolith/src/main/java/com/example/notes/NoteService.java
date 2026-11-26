package com.example.notes;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Transactional
    public Note create(String content) {
        Note note = new Note(content);
        return noteRepository.save(note);
    }
}
