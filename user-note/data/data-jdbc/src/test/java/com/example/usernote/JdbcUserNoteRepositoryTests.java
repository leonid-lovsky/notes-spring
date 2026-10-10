package com.example.usernote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcUserNoteRepositoryTests {

    @Mock
    private JdbcAggregateOperations jdbcAggregateOperations;

    private JdbcUserNoteRepository userNoteRepository;

    @BeforeEach
    void setUp() {
        userNoteRepository = new JdbcUserNoteRepository(jdbcAggregateOperations);
    }

    @Test
    void findUserNoteByIdReturnsResponseBody() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote entity = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);
        when(jdbcAggregateOperations.findById(id, UserNote.class)).thenReturn(entity);
        UserNoteResponseBody responseBody = userNoteRepository.findUserNoteById(id);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        assertEquals(expected, responseBody);
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(jdbcAggregateOperations.findById(id, UserNote.class)).thenReturn(null);

        assertThrows(UserNoteNotFoundException.class, () -> userNoteRepository.findUserNoteById(id));
    }

    @Test
    void createUserNoteInsertsEntityWithGeneratedId() {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        when(jdbcAggregateOperations.insert(any(UserNote.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UserNoteResponseBody responseBody = userNoteRepository.createUserNote(requestBody);

        assertNotNull(responseBody.id());
        assertEquals(userId, responseBody.userId());
        assertEquals(noteId, responseBody.noteId());
        assertEquals(UserNoteRole.EDITOR, responseBody.role());
    }

    @Test
    void updateUserNoteRoleByIdReplacesRole() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote existing = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);
        UserNote updated = new UserNote(id, userId, noteId, UserNoteRole.VIEWER);
        when(jdbcAggregateOperations.findById(id, UserNote.class)).thenReturn(existing);
        when(jdbcAggregateOperations.update(updated)).thenReturn(updated);
        UserNoteResponseBody responseBody = userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);
        assertEquals(expected, responseBody);
    }

    @Test
    void updateUserNoteRoleByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(jdbcAggregateOperations.findById(id, UserNote.class)).thenReturn(null);

        assertThrows(UserNoteNotFoundException.class, () -> userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER));
        verify(jdbcAggregateOperations, never()).update(any(UserNote.class));
    }

    @Test
    void deleteUserNoteByIdDeletesAndReturnsResponseBody() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote entity = new UserNote(id, userId, noteId, UserNoteRole.EDITOR);
        when(jdbcAggregateOperations.findById(id, UserNote.class)).thenReturn(entity);
        UserNoteResponseBody responseBody = userNoteRepository.deleteUserNoteById(id);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        assertEquals(expected, responseBody);
        verify(jdbcAggregateOperations).deleteById(id, UserNote.class);
    }

    @Test
    void deleteUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(jdbcAggregateOperations.findById(id, UserNote.class)).thenReturn(null);

        assertThrows(UserNoteNotFoundException.class, () -> userNoteRepository.deleteUserNoteById(id));
        verify(jdbcAggregateOperations, never()).deleteById(id, UserNote.class);
    }
}
