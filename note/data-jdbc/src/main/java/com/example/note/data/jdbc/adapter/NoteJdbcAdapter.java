package com.example.note.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteContract;
import com.example.note.data.jdbc.mapper.NoteJdbcMapperContract;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteJdbcAdapter implements NoteContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final NoteJdbcMapperContract noteJdbcMapper;

    NoteJdbcAdapter(NamedParameterJdbcTemplate jdbc, NoteJdbcMapperContract noteJdbcMapper) {
        this.jdbc = jdbc;
        this.noteJdbcMapper = noteJdbcMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO notes (id, content) VALUES (:id, :content)",
                Map.of("id", id, "content", request.content()));
        return new NoteResponse(id, request.content());
    }

    @Override
    public boolean existsById(UUID id) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM notes WHERE id = :id", Map.of("id", id),
                Integer.class);
        return count != null && count > 0;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.jdbc.query("SELECT id, content FROM notes", Map.of(), this.noteJdbcMapper::fromRow);
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, content FROM notes WHERE id = :id", Map.of("id", id), this.noteJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

    @Override
    public void remove(UUID id) {
        this.jdbc.update("DELETE FROM notes WHERE id = :id", Map.of("id", id));
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        this.jdbc.update("UPDATE notes SET content = :content WHERE id = :id",
                Map.of("id", id, "content", request.content()));
        return new NoteResponse(id, request.content());
    }

}
