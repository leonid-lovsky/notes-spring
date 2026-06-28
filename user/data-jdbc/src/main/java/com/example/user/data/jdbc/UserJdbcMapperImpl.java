package com.example.user.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Component;

@Component
class UserJdbcMapperImpl implements UserJdbcMapper {

    @Override
    public UserResponse fromRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserResponse(rs.getObject("id", UUID.class), rs.getString("username"), rs.getString("email"));
    }

}
