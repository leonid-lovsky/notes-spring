package com.example.usernote;

import java.util.UUID;

public class UserNoteService implements FindUserNoteById, CreateUserNote, UpdateUserNoteRoleById, DeleteUserNoteById {

    private final UserNoteRepository userNoteRepository;

    public UserNoteService(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public UserNoteResponseBody findUserNoteById(UUID id) {
        return userNoteRepository.findUserNoteById(id);
    }

    @Override
    public UserNoteResponseBody createUserNote(UserNoteRequestBody requestBody) {
        return userNoteRepository.createUserNote(requestBody);
    }

    @Override
    public UserNoteResponseBody updateUserNoteRoleById(UUID id, UserNoteRole role) {
        return userNoteRepository.updateUserNoteRoleById(id, role);
    }

    @Override
    public UserNoteResponseBody deleteUserNoteById(UUID id) {
        return userNoteRepository.deleteUserNoteById(id);
    }
}
