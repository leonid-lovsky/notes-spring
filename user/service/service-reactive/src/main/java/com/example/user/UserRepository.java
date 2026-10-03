package com.example.user;

import java.util.UUID;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface UserRepository extends
    ReactiveCrudRepository<User, UUID>,
    ReactiveSortingRepository<User, UUID>,
    ReactiveQueryByExampleExecutor<User> {
}
