package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class UserNoteExistsByUserIdAndNoteIdPortAdapter implements UserNoteExistsByUserIdAndNoteIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteExistsByUserIdAndNoteIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId), Integer.class);
        return count != null && count > 0;
    }

}
