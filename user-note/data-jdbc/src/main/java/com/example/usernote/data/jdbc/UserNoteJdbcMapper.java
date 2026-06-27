package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNoteResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

interface UserNoteJdbcMapper {

    UserNoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException;
}
