package com.example.note.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.note.contract.NoteExistsByIdContract;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteExistsByIdJdbcAdapter implements NoteExistsByIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    NoteExistsByIdJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsById(UUID id) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM notes WHERE id = :id", Map.of("id", id),
                Integer.class);
        return count != null && count > 0;
    }

}
