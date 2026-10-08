package com.example.usernote;

import java.util.UUID;

public class UserNoteService implements FindUserNoteById, CreateUserNoteById, ReplaceUserNoteById, UpdateUserNoteById, DeleteUserNoteById {

    private final UserNoteRepository userNoteRepository;

    public UserNoteService(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public UserNoteResponse findUserNoteById(UUID id) {
        return userNoteRepository.findById(id)
                .map(UserNoteService::toResponse)
                .orElseThrow(() -> new UserNoteNotFoundException(id));
    }

    @Override
    public UserNoteResponse createUserNoteById(UUID id, UserNoteRequest request) {
        if (userNoteRepository.existsById(id)) {
            throw new UserNoteAlreadyExistsException(id);
        }
        return toResponse(userNoteRepository.save(new UserNote(id, request.userId(), request.noteId())));
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

    private static UserNoteResponse toResponse(UserNote userNote) {
        return new UserNoteResponse(userNote.id(), userNote.userId(), userNote.noteId());
    }
}
