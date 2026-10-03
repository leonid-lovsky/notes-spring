package com.example.user;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Service;

@Service
public class UserService implements FindUserById, CreateUserById, ReplaceUserById, UpdateUserById, DeleteUserById {

    private final CrudRepository<User, UUID> userCrudRepository;
    private final PagingAndSortingRepository<User, UUID> userSortingRepository;
    private final QueryByExampleExecutor<User> userQueryByExampleExecutor;

    public UserService(
        CrudRepository<User, UUID> userCrudRepository,
        PagingAndSortingRepository<User, UUID> userSortingRepository,
        QueryByExampleExecutor<User> userQueryByExampleExecutor
    ) {
        this.userCrudRepository = userCrudRepository;
        this.userSortingRepository = userSortingRepository;
        this.userQueryByExampleExecutor = userQueryByExampleExecutor;
    }

    @Override
    public UserResponse findUserById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse createUserById(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse replaceUserById(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse updateUserById(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserResponse deleteUserById(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
