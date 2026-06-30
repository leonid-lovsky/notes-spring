package com.example.usernote.data.r2dbc.mapper;

import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import org.springframework.stereotype.Component;

@Component
class UserNoteR2dbcMapper implements UserNoteR2dbcMapperContract {

    @Override
    public UserNoteResponse fromRow(Row row, RowMetadata rowMetadata) {
        UUID userId = row.get("user_id", UUID.class);
        UUID noteId = row.get("note_id", UUID.class);
        String role = row.get("role", String.class);
        return new UserNoteResponse(userId, noteId, UserNoteRole.valueOf(role));
    }

}
