package com.example.usernote.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.data.jdbc.mapper.UserNoteRowMapperContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdContractAdapter implements UserNoteFindByUserIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteRowMapperContract userNoteRowMapper;

    UserNoteFindByUserIdContractAdapter(NamedParameterJdbcTemplate jdbc, UserNoteRowMapperContract userNoteRowMapper) {
        this.jdbc = jdbc;
        this.userNoteRowMapper = userNoteRowMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.jdbc.query("SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId",
                Map.of("userId", userId), this.userNoteRowMapper::fromRow);
    }

}
