package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JdbcUserNoteRepository.class, UserNoteTestConfiguration.class})
class UserNoteRepositoryTests {

    @Autowired
    private JdbcUserNoteRepository userNoteRepository;

    @Autowired
    private JdbcAggregateOperations operations;

    @Test
    void findUserNoteByIdReturnsStoredRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote);

        UserNoteResponseBody responseBody = userNoteRepository.findUserNoteById(id);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        assertThat(responseBody).isEqualTo(expected);
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

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);

        UserNoteResponseBody responseBody = userNoteRepository.createUserNote(requestBody);

        UUID id = responseBody.id();

        UserNote userNote = operations.findById(id, UserNote.class);

        UserNote expected = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        assertThat(userNote).isEqualTo(expected);
    }

    @Test
    void updateUserNoteRoleByIdReturnsUpdatedRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote);

        UserNoteResponseBody responseBody = userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);

        assertThat(responseBody).isEqualTo(expected);
    }

    @Test
    void updateUserNoteRoleByIdStoresNewRole() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote);

        userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        UserNote updatedUserNote = operations.findById(id, UserNote.class);

        UserNote expected = new UserNote(id, userId, noteId, UserNoteRole.VIEWER);

        assertThat(updatedUserNote).isEqualTo(expected);
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

        UserNote userNote = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);

        operations.insert(userNote);

        UserNoteResponseBody responseBody = userNoteRepository.deleteUserNoteById(id);

        boolean exists = operations.existsById(id, UserNote.class);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        assertThat(responseBody).isEqualTo(expected);

        assertThat(exists).isFalse();
    }

    @Test
    void deleteUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> userNoteRepository.deleteUserNoteById(id))
            .isInstanceOf(UserNoteNotFoundException.class);
    }
}
