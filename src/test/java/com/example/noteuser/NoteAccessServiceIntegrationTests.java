package com.example.noteuser;

import com.example.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Note Access Service Integration Tests")
class NoteAccessServiceIntegrationTests extends IntegrationTestSupport {

    @Autowired
    private NoteAccessService noteAccessService;

    @Test
    @DisplayName("Grant creator access")
    void grantCreatorAccess() {
        UUID noteId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        noteAccessService.grantCreatorAccess(noteId, userId);

        assertThat(noteAccessService.getNoteIdsForUser(userId)).containsExactly(noteId);
        noteAccessService.ensureUserCanRead(noteId, userId);
        noteAccessService.ensureUserCanEdit(noteId, userId);
        noteAccessService.ensureUserCanDelete(noteId, userId);
    }

    @Test
    @DisplayName("Reject access for unknown user")
    void rejectAccessForUnknownUser() {
        assertThatThrownBy(() -> noteAccessService.ensureUserCanRead(UUID.randomUUID(), UUID.randomUUID()))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("403 FORBIDDEN");
    }
}
