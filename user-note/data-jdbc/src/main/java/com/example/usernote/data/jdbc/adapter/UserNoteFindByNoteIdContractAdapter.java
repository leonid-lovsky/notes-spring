package com.example.usernote.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByNoteIdContract;
import com.example.usernote.data.jdbc.mapper.UserNoteRowMapperContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdContractAdapter implements UserNoteFindByNoteIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteRowMapperContract userNoteRowMapper;

    UserNoteFindByNoteIdContractAdapter(NamedParameterJdbcTemplate jdbc, UserNoteRowMapperContract userNoteRowMapper) {
        this.jdbc = jdbc;
        this.userNoteRowMapper = userNoteRowMapper;
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.jdbc.query("SELECT user_id, note_id, role FROM user_notes WHERE note_id = :noteId",
                Map.of("noteId", noteId), this.userNoteRowMapper::fromRow);
    }

}
