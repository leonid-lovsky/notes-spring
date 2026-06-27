package com.example.note.data.jdbc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteFindByIdPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteFindByIdPortAdapter implements NoteFindByIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    NoteFindByIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Note> findById(UUID id) {
        return jdbc.query("SELECT id, content FROM notes WHERE id = :id",
                Map.of("id", id), NoteFindByIdPortAdapter::toNote).stream().findFirst();
    }

    private static Note toNote(ResultSet rs, int row) throws SQLException {
        return new Note(rs.getObject("id", UUID.class), rs.getString("content"));
    }
}
