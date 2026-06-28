package com.example.usernote.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdAndNoteIdContract;
import com.example.usernote.data.jdbc.mapper.UserNoteRowMapperContract;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdContractAdapter implements UserNoteFindByUserIdAndNoteIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteRowMapperContract userNoteRowMapper;

    UserNoteFindByUserIdAndNoteIdContractAdapter(NamedParameterJdbcTemplate jdbc,
            UserNoteRowMapperContract userNoteRowMapper) {
        this.jdbc = jdbc;
        this.userNoteRowMapper = userNoteRowMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.jdbc
            .query("SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                    Map.of("userId", userId, "noteId", noteId), this.userNoteRowMapper::fromRow)
            .stream()
            .findFirst();
    }

}
