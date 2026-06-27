package com.example.user.data.jdbc;

import com.example.user.domain.UserResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

interface UserJdbcMapper {

    UserResponse fromRow(ResultSet rs, int rowNum) throws SQLException;
}
