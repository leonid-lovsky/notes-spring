package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserNoteService implements ReactiveFindUserNoteById, ReactiveCreateUserNoteById, ReactiveReplaceUserNoteById, ReactiveUpdateUserNoteById, ReactiveDeleteUserNoteById {

    private final ReactiveCrudRepository<UserNote, UUID> userNoteCrudRepository;
    private final ReactiveSortingRepository<UserNote, UUID> userNoteSortingRepository;
    private final ReactiveQueryByExampleExecutor<UserNote> userNoteQueryByExampleExecutor;

    public UserNoteService(
        ReactiveCrudRepository<UserNote, UUID> userNoteCrudRepository,
        ReactiveSortingRepository<UserNote, UUID> userNoteSortingRepository,
        ReactiveQueryByExampleExecutor<UserNote> userNoteQueryByExampleExecutor
    ) {
        this.userNoteCrudRepository = userNoteCrudRepository;
        this.userNoteSortingRepository = userNoteSortingRepository;
        this.userNoteQueryByExampleExecutor = userNoteQueryByExampleExecutor;
    }

    @Override
    public Mono<UserNoteResponse> findUserNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> createUserNoteById(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> replaceUserNoteById(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> updateUserNoteById(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> deleteUserNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
