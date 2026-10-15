package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({ReactiveMongoUserNoteRepository.class, UserNoteTestConfiguration.class})
class ReactiveUserNoteRepositoryTests {

    @Autowired
    private ReactiveMongoUserNoteRepository userNoteRepository;

    @Autowired
    private ReactiveMongoOperations operations;

    @Test
    void findUserNoteByIdReturnsStoredRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote).block();

        Mono<UserNoteResponseBody> result = userNoteRepository.findUserNoteById(id);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        StepVerifier.create(result).expectNext(expected).verifyComplete();
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        Mono<UserNoteResponseBody> result = userNoteRepository.findUserNoteById(id);

        StepVerifier.create(result).expectError(UserNoteNotFoundException.class).verify();
    }

    @Test
    void createUserNoteStoresRecord() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);

        UserNoteResponseBody responseBody = Objects.requireNonNull(userNoteRepository.createUserNote(requestBody).block());

        UUID id = responseBody.id();

        UserNote userNote = operations.findById(id, UserNote.class).block();

        UserNote expected = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        assertThat(userNote).isEqualTo(expected);
    }

    @Test
    void updateUserNoteRoleByIdReturnsUpdatedRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote).block();

        Mono<UserNoteResponseBody> result = userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);

        StepVerifier.create(result).expectNext(expected).verifyComplete();
    }

    @Test
    void updateUserNoteRoleByIdStoresNewRole() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote).block();

        userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER).block();

        UserNote updatedUserNote = operations.findById(id, UserNote.class).block();

        UserNote expected = new UserNote(id, userId, noteId, UserNoteRole.VIEWER);

        assertThat(updatedUserNote).isEqualTo(expected);
    }

    @Test
    void updateUserNoteRoleByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        Mono<UserNoteResponseBody> result = userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        StepVerifier.create(result).expectError(UserNoteNotFoundException.class).verify();
    }

    @Test
    void deleteUserNoteByIdRemovesRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote).block();

        UserNoteResponseBody responseBody = userNoteRepository.deleteUserNoteById(id).block();

        Query query = Query.query(Criteria.where("_id").is(id));

        Boolean exists = operations.exists(query, UserNote.class).block();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        assertThat(responseBody).isEqualTo(expected);

        assertThat(exists).isFalse();
    }

    @Test
    void deleteUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        Mono<UserNoteResponseBody> result = userNoteRepository.deleteUserNoteById(id);

        StepVerifier.create(result).expectError(UserNoteNotFoundException.class).verify();
    }
}
