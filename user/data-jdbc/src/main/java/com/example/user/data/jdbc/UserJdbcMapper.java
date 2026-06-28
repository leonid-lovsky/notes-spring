package com.example.user.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.user.domain.UserResponse;

interface UserJdbcMapper {

    UserResponse fromRow(ResultSet rs, int rowNum) throws SQLException;

}
