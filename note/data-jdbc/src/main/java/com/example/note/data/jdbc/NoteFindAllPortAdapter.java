package com.example.note.data.jdbc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteFindAllPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

    private final NamedParameterJdbcTemplate jdbc;

    NoteFindAllPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Note> findAll() {
        return jdbc.query("SELECT id, content FROM notes", Map.of(), NoteFindAllPortAdapter::toNote);
    }

    private static Note toNote(ResultSet rs, int row) throws SQLException {
        return new Note(rs.getObject("id", UUID.class), rs.getString("content"));
    }
}
