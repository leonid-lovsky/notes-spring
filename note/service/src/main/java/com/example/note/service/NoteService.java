package com.example.note.service;

import com.example.note.domain.Note;
import com.example.note.domain.NoteOutputPort;
import com.example.note.domain.NoteUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
class NoteService implements NoteUseCase {

    private final NoteOutputPort noteOutputPort;

    NoteService(NoteOutputPort noteOutputPort) {
        this.noteOutputPort = noteOutputPort;
    }

    @Override
    public Note create(String content) {
        Note note = new Note(UUID.randomUUID(), content);
        noteOutputPort.add(note);
        return note;
    }

    @Override
    @Transactional(readOnly = true)
    public Note findById(UUID id) {
        return noteOutputPort.findById(id)
            .orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> findAll() {
        return noteOutputPort.findAll();
    }

    @Override
    public Note update(UUID id, String content) {
        Note note = noteOutputPort.findById(id)
            .orElseThrow(() -> new NoSuchElementException(id.toString()));
        Note updated = note.withContent(content);
        noteOutputPort.replace(updated);
        return updated;
    }

    @Override
    public void delete(UUID id) {
        if (!noteOutputPort.existsById(id)) {
            throw new NoSuchElementException(id.toString());
        }
        noteOutputPort.remove(id);
    }
}
