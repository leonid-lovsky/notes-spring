package com.example.usernote.data.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteJdbcMapperContract {

    UserNoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException;
}
