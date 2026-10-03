package com.example.user;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserRepository extends
    CrudRepository<User, UUID>,
    PagingAndSortingRepository<User, UUID>,
    QueryByExampleExecutor<User> {
}
