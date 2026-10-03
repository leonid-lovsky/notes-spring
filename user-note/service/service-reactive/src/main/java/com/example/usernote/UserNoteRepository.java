package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface UserNoteRepository extends
    ReactiveCrudRepository<UserNote, UUID>,
    ReactiveSortingRepository<UserNote, UUID>,
    ReactiveQueryByExampleExecutor<UserNote> {
}
