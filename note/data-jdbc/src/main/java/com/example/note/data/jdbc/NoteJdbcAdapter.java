package com.example.note.data.jdbc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
class NoteJdbcAdapter implements NoteRepository {

    private final NamedParameterJdbcTemplate jdbc;

    NoteJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM notes WHERE id = :id";
        Integer count = jdbc.queryForObject(sql, Map.of("id", id), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Optional<Note> findById(UUID id) {
        String sql = "SELECT id, content FROM notes WHERE id = :id";
        List<Note> results = jdbc.query(sql, Map.of("id", id), NoteJdbcAdapter::toNote);
        return results.stream().findFirst();
    }

    @Override
    public List<Note> findAll() {
        return jdbc.query("SELECT id, content FROM notes", NoteJdbcAdapter::toNote);
    }

    @Override
    public Note add(Note note) {
        UUID id = UUID.randomUUID();
        String sql = "INSERT INTO notes (id, content) VALUES (:id, :content)";
        jdbc.update(sql, Map.of("id", id, "content", note.content()));
        return new Note(id, note.content());
    }

    @Override
    public void replace(Note note) {
        String sql = "UPDATE notes SET content = :content WHERE id = :id";
        jdbc.update(sql, Map.of("id", note.id(), "content", note.content()));
    }

    @Override
    public void remove(UUID id) {
        jdbc.update("DELETE FROM notes WHERE id = :id", Map.of("id", id));
    }

    private static Note toNote(ResultSet rs, int rowNum) throws SQLException {
        return new Note(
                rs.getObject("id", UUID.class),
                rs.getString("content")
        );
    }
}
