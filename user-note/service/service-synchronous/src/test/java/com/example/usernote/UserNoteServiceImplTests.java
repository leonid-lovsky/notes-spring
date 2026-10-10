package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNoteServiceImplTests {

    @Mock
    private UserNoteRepository userNoteRepository;

    @InjectMocks
    private UserNoteServiceImpl userNoteService;

    @Test
    void findUserNoteByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        when(userNoteRepository.findUserNoteById(id)).thenReturn(expected);

        UserNoteResponseBody responseBody = userNoteService.findUserNoteById(id);

        assertThat(responseBody).isEqualTo(expected);
    }

    @Test
    void createUserNoteReturnsRepositoryResponse() {
        UserNoteRequestBody requestBody = new UserNoteRequestBody(UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        UserNoteResponseBody expected = new UserNoteResponseBody(UUID.randomUUID(), requestBody.userId(), requestBody.noteId(), UserNoteRole.EDITOR);

        when(userNoteRepository.createUserNote(requestBody)).thenReturn(expected);

        UserNoteResponseBody responseBody = userNoteService.createUserNote(requestBody);

        assertThat(responseBody).isEqualTo(expected);
    }

    @Test
    void updateUserNoteRoleByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        when(userNoteRepository.updateUserNoteRoleById(id, UserNoteRole.EDITOR)).thenReturn(expected);

        UserNoteResponseBody responseBody = userNoteService.updateUserNoteRoleById(id, UserNoteRole.EDITOR);

        assertThat(responseBody).isEqualTo(expected);
    }

    @Test
    void deleteUserNoteByIdReturnsRepositoryResponse() {
        UUID id = UUID.randomUUID();

        UserNoteResponseBody expected = new UserNoteResponseBody(id, UUID.randomUUID(), UUID.randomUUID(), UserNoteRole.EDITOR);
        when(userNoteRepository.deleteUserNoteById(id)).thenReturn(expected);

        UserNoteResponseBody responseBody = userNoteService.deleteUserNoteById(id);

        assertThat(responseBody).isEqualTo(expected);
    }
}
