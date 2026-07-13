package com.example.usernote.data.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;

import org.springframework.stereotype.Component;

@Component
class UserNoteJdbcMapper implements UserNoteJdbcMapperContract {

    @Override
    public UserNoteResponse fromRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserNoteResponse(rs.getObject("id", UUID.class), rs.getObject("user_id", UUID.class),
                rs.getObject("note_id", UUID.class), UserNoteRole.valueOf(rs.getString("role")));
    }
}
