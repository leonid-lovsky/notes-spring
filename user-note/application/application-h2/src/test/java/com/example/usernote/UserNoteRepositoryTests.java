package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@Import(JdbcUserNoteRepository.class)
class UserNoteRepositoryTests {

    @Autowired
    private JdbcUserNoteRepository userNoteRepository;

    @Autowired
    private JdbcAggregateOperations aggregateOperations;

    @Test
    void findUserNoteByIdReturnsStoredRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        aggregateOperations.insert(new UserNote(id, userId, noteId, UserNoteRole.EDITOR));

        UserNoteResponseBody responseBody = userNoteRepository.findUserNoteById(id);

        assertThat(responseBody).isEqualTo(new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR));
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> userNoteRepository.findUserNoteById(id))
            .isInstanceOf(UserNoteNotFoundException.class);
    }

    @Test
    void createUserNoteStoresRecord() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody responseBody = userNoteRepository.createUserNote(new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR));

        UserNote stored = aggregateOperations.findById(responseBody.id(), UserNote.class);

        assertThat(stored).isEqualTo(new UserNote(responseBody.id(), userId, noteId, UserNoteRole.EDITOR));
    }

    @Test
    void updateUserNoteRoleByIdStoresNewRole() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        aggregateOperations.insert(new UserNote(id, userId, noteId, UserNoteRole.EDITOR));

        UserNoteResponseBody responseBody = userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        UserNote stored = aggregateOperations.findById(id, UserNote.class);

        assertThat(responseBody.role()).isEqualTo(UserNoteRole.VIEWER);
        assertThat(stored).isEqualTo(new UserNote(id, userId, noteId, UserNoteRole.VIEWER));
    }

    @Test
    void updateUserNoteRoleByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER))
            .isInstanceOf(UserNoteNotFoundException.class);
    }

    @Test
    void deleteUserNoteByIdRemovesRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        aggregateOperations.insert(new UserNote(id, userId, noteId, UserNoteRole.EDITOR));

        UserNoteResponseBody responseBody = userNoteRepository.deleteUserNoteById(id);

        assertThat(responseBody).isEqualTo(new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR));
        assertThat(aggregateOperations.existsById(id, UserNote.class)).isFalse();
    }

    @Test
    void deleteUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> userNoteRepository.deleteUserNoteById(id))
            .isInstanceOf(UserNoteNotFoundException.class);
    }
}
