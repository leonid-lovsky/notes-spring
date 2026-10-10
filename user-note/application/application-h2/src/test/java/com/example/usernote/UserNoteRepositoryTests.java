package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(UserNoteTestConfiguration.class)
class UserNoteRepositoryTests {

    @Autowired
    private UserNoteRepository userNoteRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @ParameterizedTest
    @EnumSource(UserNoteRole.class)
    void findUserNoteByIdReturnsStoredRecord(UserNoteRole role) {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        jdbcClient.sql("INSERT INTO \"user_note\" (\"id\", \"user_id\", \"note_id\", \"role\") VALUES (?, ?, ?, ?)")
            .params(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "EDITOR").update();

        jdbcClient.sql("INSERT INTO \"user_note\" (\"id\", \"user_id\", \"note_id\", \"role\") VALUES (?, ?, ?, ?)")
            .params(id, userId, noteId, role.name()).update();

        UserNoteResponseBody responseBody = userNoteRepository.findUserNoteById(id);

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, role);

        assertEquals(expected, responseBody);
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(UserNoteNotFoundException.class, () -> userNoteRepository.findUserNoteById(id));
    }

    @ParameterizedTest
    @EnumSource(UserNoteRole.class)
    void createUserNoteStoresRecord(UserNoteRole role) {
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, role);

        UserNoteResponseBody responseBody = userNoteRepository.createUserNote(requestBody);

        Map<String, Object> row = jdbcClient.sql("SELECT \"user_id\", \"note_id\", \"role\" FROM \"user_note\" WHERE \"id\" = ?")
            .param(responseBody.id()).query().singleRow();

        assertNotNull(responseBody.id());
        assertEquals(userId, row.get("user_id"));
        assertEquals(noteId, row.get("note_id"));
        assertEquals(role.name(), row.get("role"));
    }

    @Test
    void updateUserNoteRoleByIdStoresRoleOnlyForRequestedRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();

        jdbcClient.sql("INSERT INTO \"user_note\" (\"id\", \"user_id\", \"note_id\", \"role\") VALUES (?, ?, ?, ?)")
            .params(otherId, UUID.randomUUID(), UUID.randomUUID(), "EDITOR").update();

        jdbcClient.sql("INSERT INTO \"user_note\" (\"id\", \"user_id\", \"note_id\", \"role\") VALUES (?, ?, ?, ?)")
            .params(id, userId, noteId, "EDITOR").update();

        UserNoteResponseBody responseBody = userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        String storedRole = jdbcClient.sql("SELECT \"role\" FROM \"user_note\" WHERE \"id\" = ?")
            .param(id).query(String.class).single();

        String otherRole = jdbcClient.sql("SELECT \"role\" FROM \"user_note\" WHERE \"id\" = ?")
            .param(otherId).query(String.class).single();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);

        assertEquals(expected, responseBody);
        assertEquals("VIEWER", storedRole);
        assertEquals("EDITOR", otherRole);
    }

    @Test
    void updateUserNoteRoleByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(UserNoteNotFoundException.class, () -> userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER));
    }

    @Test
    void deleteUserNoteByIdRemovesOnlyRequestedRecord() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();

        jdbcClient.sql("INSERT INTO \"user_note\" (\"id\", \"user_id\", \"note_id\", \"role\") VALUES (?, ?, ?, ?)")
            .params(otherId, UUID.randomUUID(), UUID.randomUUID(), "EDITOR").update();

        jdbcClient.sql("INSERT INTO \"user_note\" (\"id\", \"user_id\", \"note_id\", \"role\") VALUES (?, ?, ?, ?)")
            .params(id, userId, noteId, "EDITOR").update();

        UserNoteResponseBody responseBody = userNoteRepository.deleteUserNoteById(id);

        Long count = jdbcClient.sql("SELECT COUNT(*) FROM \"user_note\" WHERE \"id\" = ?")
            .param(id).query(Long.class).single();

        Long otherCount = jdbcClient.sql("SELECT COUNT(*) FROM \"user_note\" WHERE \"id\" = ?")
            .param(otherId).query(Long.class).single();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        assertEquals(expected, responseBody);
        assertEquals(0L, count);
        assertEquals(1L, otherCount);
    }

    @Test
    void deleteUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(UserNoteNotFoundException.class, () -> userNoteRepository.deleteUserNoteById(id));
    }
}
