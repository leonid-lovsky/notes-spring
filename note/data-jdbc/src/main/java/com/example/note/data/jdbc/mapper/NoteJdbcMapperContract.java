package com.example.note.data.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.note.domain.NoteResponse;

public interface NoteJdbcMapperContract {

    NoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException;
}
