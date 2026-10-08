package com.example.usernote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNoteServiceTests {

    @Mock
    private UserNoteRepository userNoteRepository;

    private UserNoteService userNoteService;

    @BeforeEach
    void setUp() {
        userNoteService = new UserNoteService(userNoteRepository);
    }

    @Test
    void findUserNoteByIdReturnsResponseWhenFound() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId);
        when(userNoteRepository.findById(id)).thenReturn(Optional.of(userNote));
        UserNoteResponse response = userNoteService.findUserNoteById(id);

        UserNoteResponse expected = new UserNoteResponse(id, userId, noteId);

        assertEquals(expected, response);
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(userNoteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNoteNotFoundException.class, () -> userNoteService.findUserNoteById(id));
    }

    @Test
    void createUserNoteByIdSavesAndReturnsResponse() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNote userNote = new UserNote(id, userId, noteId);
        UserNoteRequest request = new UserNoteRequest(userId, noteId);
        when(userNoteRepository.existsById(id)).thenReturn(false);
        when(userNoteRepository.save(userNote)).thenReturn(userNote);
        UserNoteResponse response = userNoteService.createUserNoteById(id, request);

        UserNoteResponse expected = new UserNoteResponse(id, userId, noteId);

        assertEquals(expected, response);
    }

    @Test
    void createUserNoteByIdThrowsWhenAlreadyExists() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID noteId = UUID.randomUUID();

        UserNoteRequest request = new UserNoteRequest(userId, noteId);
        when(userNoteRepository.existsById(id)).thenReturn(true);

        assertThrows(UserNoteAlreadyExistsException.class, () -> userNoteService.createUserNoteById(id, request));

        verify(userNoteRepository, never()).save(any());
    }
}
