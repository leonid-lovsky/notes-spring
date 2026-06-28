package com.example.user.data.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.user.domain.UserResponse;

public interface UserRowMapperContract {

    UserResponse fromRow(ResultSet rs, int rowNum) throws SQLException;

}
