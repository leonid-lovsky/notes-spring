package com.example.note.service;

import com.example.note.domain.Note;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRepository;
import com.example.note.domain.NoteUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class NoteService implements NoteUseCase {

    private final NoteRepository noteRepository;

    NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note create(String content) {
        Note note = new Note(UUID.randomUUID(), content);
        noteRepository.add(note);
        return note;
    }

    @Override
    @Transactional(readOnly = true)
    public Note findById(UUID id) {
        return noteRepository.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public Note update(UUID id, String content) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        Note updated = new Note(id, content);
        noteRepository.replace(updated);
        return updated;
    }

    @Override
    public void delete(UUID id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.remove(id);
    }
}
