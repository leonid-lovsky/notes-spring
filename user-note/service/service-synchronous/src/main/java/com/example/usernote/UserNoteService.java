package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Service;

@Service
public class UserNoteService implements FindUserNoteById, CreateUserNoteById, ReplaceUserNoteById, UpdateUserNoteById, DeleteUserNoteById {

    private final CrudRepository<UserNote, UUID> userNoteCrudRepository;
    private final PagingAndSortingRepository<UserNote, UUID> userNoteSortingRepository;
    private final QueryByExampleExecutor<UserNote> userNoteQueryByExampleExecutor;

    public UserNoteService(
        CrudRepository<UserNote, UUID> userNoteCrudRepository,
        PagingAndSortingRepository<UserNote, UUID> userNoteSortingRepository,
        QueryByExampleExecutor<UserNote> userNoteQueryByExampleExecutor
    ) {
        this.userNoteCrudRepository = userNoteCrudRepository;
        this.userNoteSortingRepository = userNoteSortingRepository;
        this.userNoteQueryByExampleExecutor = userNoteQueryByExampleExecutor;
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
