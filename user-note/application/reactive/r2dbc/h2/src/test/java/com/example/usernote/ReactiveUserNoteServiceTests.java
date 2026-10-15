package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import({ReactiveUserNoteServiceImpl.class, R2dbcUserNoteRepository.class})
class ReactiveUserNoteServiceTests {

    @Autowired
    private ReactiveUserNoteService userNoteService;

    @Autowired
    private R2dbcEntityTemplate operations;

    @Test
    void findUserNoteByIdReturnsStoredRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote).block();

        Mono<UserNoteResponseBody> result = userNoteService.findUserNoteById(id);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        StepVerifier.create(result).expectNext(expected).verifyComplete();
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        Mono<UserNoteResponseBody> result = userNoteService.findUserNoteById(id);

        StepVerifier.create(result).expectError(UserNoteNotFoundException.class).verify();
    }

    @Test
    void createUserNoteStoresRecord() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);

        UserNoteResponseBody responseBody = Objects.requireNonNull(userNoteService.createUserNote(requestBody).block());

        UUID id = responseBody.id();

        Query query = Query.query(Criteria.where("id").is(id));

        UserNote userNote = operations.selectOne(query, UserNote.class).block();

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

        Mono<UserNoteResponseBody> result = userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

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

        userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER).block();

        Query query = Query.query(Criteria.where("id").is(id));

        UserNote updatedUserNote = operations.selectOne(query, UserNote.class).block();

        UserNote expected = new UserNote(id, userId, noteId, UserNoteRole.VIEWER);

        assertThat(updatedUserNote).isEqualTo(expected);
    }

    @Test
    void updateUserNoteRoleByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        Mono<UserNoteResponseBody> result = userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        StepVerifier.create(result).expectError(UserNoteNotFoundException.class).verify();
    }

    @Test
    void deleteUserNoteByIdRemovesRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote).block();

        UserNoteResponseBody responseBody = userNoteService.deleteUserNoteById(id).block();

        Query query = Query.query(Criteria.where("id").is(id));

        Boolean exists = operations.exists(query, UserNote.class).block();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        assertThat(responseBody).isEqualTo(expected);

        assertThat(exists).isFalse();
    }

    @Test
    void deleteUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        Mono<UserNoteResponseBody> result = userNoteService.deleteUserNoteById(id);

        StepVerifier.create(result).expectError(UserNoteNotFoundException.class).verify();
    }
}
