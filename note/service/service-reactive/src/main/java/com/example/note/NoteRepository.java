package com.example.note;

import java.util.UUID;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface NoteRepository extends
    ReactiveCrudRepository<Note, UUID>,
    ReactiveSortingRepository<Note, UUID>,
    ReactiveQueryByExampleExecutor<Note> {
}
