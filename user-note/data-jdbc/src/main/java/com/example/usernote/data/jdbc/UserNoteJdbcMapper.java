package com.example.usernote.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.usernote.domain.UserNoteResponse;

interface UserNoteJdbcMapper {

    UserNoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException;

}
