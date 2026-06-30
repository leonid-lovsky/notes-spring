package com.example.usernote.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.data.jdbc.mapper.UserNoteJdbcMapperContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdJdbcAdapter implements UserNoteFindByUserIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteJdbcMapperContract userNoteJdbcMapper;

    UserNoteFindByUserIdJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapperContract userNoteJdbcMapper) {
        this.jdbc = jdbc;
        this.userNoteJdbcMapper = userNoteJdbcMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.jdbc.query("SELECT id, user_id, note_id, role FROM user_notes WHERE user_id = :userId",
                Map.of("userId", userId), this.userNoteJdbcMapper::fromRow);
    }

}
