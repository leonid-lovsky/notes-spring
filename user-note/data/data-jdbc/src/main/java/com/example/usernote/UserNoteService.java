package com.example.usernote;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserNoteService implements FindUserNoteById, CreateUserNoteById, ReplaceUserNoteById, UpdateUserNoteById, DeleteUserNoteById {

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
