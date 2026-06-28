package com.example.note.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.note.domain.NoteResponse;

interface NoteJdbcMapper {

    NoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException;

}
