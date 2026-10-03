package com.example.usernote;

import java.util.UUID;

public class UserNoteService implements FindUserNoteById, CreateUserNoteById, ReplaceUserNoteById, UpdateUserNoteById, DeleteUserNoteById {

    private final UserNoteRepository userNoteRepository;

    public UserNoteService(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public UserNoteResponse findUserNoteById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserNoteResponse createUserNoteById(UUID id, UserNoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserNoteResponse replaceUserNoteById(UUID id, UserNoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserNoteResponse updateUserNoteById(UUID id, UserNoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserNoteResponse deleteUserNoteById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
