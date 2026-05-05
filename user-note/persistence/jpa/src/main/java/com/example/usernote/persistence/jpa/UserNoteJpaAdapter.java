package com.example.usernote.persistence.jpa;

import com.example.crud.persistence.jpa.CrudJpaAdapter;
import com.example.usernote.contract.UserNote;
import com.example.usernote.contract.UserNoteNotFoundException;
import com.example.usernote.contract.UserNoteStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserNoteJpaAdapter extends CrudJpaAdapter<UserNote, UserNoteEntity, UUID> implements UserNoteStore {

    public UserNoteJpaAdapter(UserNoteRepository repository, UserNoteJpaMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public UserNote create(UserNote userNote) {
        return super.create(userNote);
    }

    @Override
    public List<UserNote> readAll() {
        return super.readAll();
    }

    @Override
    public Optional<UserNote> readById(UUID id) {
        return super.readById(id);
    }

    @Override
    public UserNote update(UserNote userNote) {
        return super.update(userNote);
    }

    @Override
    public UserNote replace(UserNote userNote) {
        return super.replace(userNote);
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected UUID getId(UserNote userNote) {
        return userNote.id();
    }

    @Override
    protected RuntimeException newMissingModelException(UUID id) {
        return new UserNoteNotFoundException(id);
    }
}
