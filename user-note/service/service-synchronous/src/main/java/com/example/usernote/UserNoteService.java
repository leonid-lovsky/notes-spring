package com.example.usernote;

import java.util.UUID;

public class UserNoteService implements FindUserNoteById, CreateUserNoteById, ReplaceUserNoteById, UpdateUserNoteById, DeleteUserNoteById {

    private final UserNoteMapper userNoteMapper = new UserNoteMapperImpl();

    private final UserNoteRepository userNoteRepository;

    public UserNoteService(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public UserNoteResponse findUserNoteById(UUID id) {
        return userNoteRepository.findById(id)
                .map(userNoteMapper::toResponse)
                .orElseThrow(() -> new UserNoteNotFoundException(id));
    }

    @Override
    public UserNoteResponse createUserNoteById(UUID id, UserNoteRequest request) {
        if (userNoteRepository.existsById(id)) {
            throw new UserNoteAlreadyExistsException(id);
        }

        UserNote userNote = userNoteMapper.toEntity(id, request);
        UserNote savedUserNote = userNoteRepository.save(userNote);

        return userNoteMapper.toResponse(savedUserNote);
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
