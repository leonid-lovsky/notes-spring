package com.example.usernote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNoteServiceImplTests {

    @Mock
    private UserNoteRepository userNoteRepository;

    private UserNoteServiceImpl userNoteService;

    @BeforeEach
    void setUp() {
        userNoteService = new UserNoteServiceImpl(userNoteRepository);
    }

    @Test
    void findUserNoteByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteRepository.findUserNoteById(id)).thenReturn(expected);
        UserNoteResponseBody responseBody = userNoteService.findUserNoteById(id);

        assertEquals(expected, responseBody);
    }

    @Test
    void createUserNoteReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteRepository.createUserNote(requestBody)).thenReturn(expected);
        UserNoteResponseBody responseBody = userNoteService.createUserNote(requestBody);

        assertEquals(expected, responseBody);
    }

    @Test
    void updateUserNoteRoleByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.VIEWER);
        when(userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.VIEWER)).thenReturn(expected);
        UserNoteResponseBody responseBody = userNoteService.updateUserNoteRoleById(id, UserNoteRole.VIEWER);

        assertEquals(expected, responseBody);
    }

    @Test
    void deleteUserNoteByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);
        when(userNoteRepository.deleteUserNoteById(id)).thenReturn(expected);
        UserNoteResponseBody responseBody = userNoteService.deleteUserNoteById(id);

        assertEquals(expected, responseBody);
    }
}
