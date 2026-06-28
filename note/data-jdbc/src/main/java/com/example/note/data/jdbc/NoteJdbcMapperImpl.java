package com.example.note.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Component;

@Component
class NoteJdbcMapperImpl implements NoteJdbcMapper {

    @Override
    public NoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException {
        return new NoteResponse(rs.getObject("id", UUID.class), rs.getString("content"));
    }

}
