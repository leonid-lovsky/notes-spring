package com.example.note.data.jdbc;

import com.example.note.domain.NoteResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

interface NoteJdbcMapper {

    NoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException;
}
