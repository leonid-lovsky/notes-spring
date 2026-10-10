package com.example.usernote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveUserNoteServiceImplTests {

    @Mock
    private ReactiveUserNoteRepository userNoteRepository;

    private ReactiveUserNoteServiceImpl userNoteService;

    @BeforeEach
    void setUp() {
        userNoteService = new ReactiveUserNoteServiceImpl(userNoteRepository);
    }

    @Test
    void findUserNoteByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteRepository.findUserNoteById(id)).thenReturn(Mono.just(expected));

        UserNoteResponseBody responseBody = userNoteService.findUserNoteById(id).block();

        assertEquals(expected, responseBody);
    }

    @Test
    void createUserNoteReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequestBody requestBody = new UserNoteRequestBody(userId, noteId, UserNoteRole.EDITOR);
        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteRepository.createUserNote(requestBody)).thenReturn(Mono.just(expected));

        UserNoteResponseBody responseBody = userNoteService.createUserNote(requestBody).block();

        assertEquals(expected, responseBody);
    }

    @Test
    void updateUserNoteRoleByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.EDITOR)).thenReturn(Mono.just(expected));

        UserNoteResponseBody responseBody = userNoteService.updateUserNoteRoleById(id, UserNoteRole.EDITOR).block();

        assertEquals(expected, responseBody);
    }

    @Test
    void deleteUserNoteByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, userId, noteId, UserNoteRole.EDITOR);

        when(userNoteRepository.deleteUserNoteById(id)).thenReturn(Mono.just(expected));

        UserNoteResponseBody responseBody = userNoteService.deleteUserNoteById(id).block();

        assertEquals(expected, responseBody);
    }
}
