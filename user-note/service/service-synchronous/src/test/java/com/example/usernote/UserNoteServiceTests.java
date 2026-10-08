package com.example.usernote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(userNoteRepository.findById(id)).thenReturn(Optional.of(new UserNote(id, userId, noteId)));

        UserNoteResponse response = userNoteService.findUserNoteById(id);

        assertEquals(new UserNoteResponse(id, userId, noteId), response);
    }

    @Test
    void findUserNoteByIdThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userNoteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNoteNotFoundException.class, () -> userNoteService.findUserNoteById(id));
    }
}
