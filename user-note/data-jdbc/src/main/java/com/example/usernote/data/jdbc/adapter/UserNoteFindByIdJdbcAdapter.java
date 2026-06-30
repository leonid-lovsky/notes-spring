package com.example.usernote.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByIdContract;
import com.example.usernote.data.jdbc.mapper.UserNoteJdbcMapperContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByIdJdbcAdapter implements UserNoteFindByIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteJdbcMapperContract userNoteJdbcMapper;

    UserNoteFindByIdJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapperContract userNoteJdbcMapper) {
        this.jdbc = jdbc;
        this.userNoteJdbcMapper = userNoteJdbcMapper;
    }

    @Override
    public Optional<UserNoteResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, user_id, note_id, role FROM user_notes WHERE id = :id", Map.of("id", id),
                    this.userNoteJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

}
