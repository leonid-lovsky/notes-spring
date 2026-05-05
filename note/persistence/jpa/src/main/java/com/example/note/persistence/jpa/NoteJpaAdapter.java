package com.example.note.persistence.jpa;

import com.example.crud.persistence.jpa.CrudJpaAdapter;
import com.example.note.contract.Note;
import com.example.note.contract.NoteNotFoundException;
import com.example.note.contract.NoteStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NoteJpaAdapter extends CrudJpaAdapter<Note, NoteEntity, UUID> implements NoteStore {

    public NoteJpaAdapter(NoteRepository repository, NoteJpaMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public Note create(Note note) {
        return super.create(note);
    }

    @Override
    public List<Note> readAll() {
        return super.readAll();
    }

    @Override
    public Optional<Note> readById(UUID id) {
        return super.readById(id);
    }

    @Override
    public Note update(Note note) {
        return super.update(note);
    }

    @Override
    public Note replace(Note note) {
        return super.replace(note);
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected UUID getId(Note note) {
        return note.id();
    }

    @Override
    protected RuntimeException newMissingModelException(UUID id) {
        return new NoteNotFoundException(id);
    }
}
