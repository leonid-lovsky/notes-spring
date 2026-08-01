package com.example.user.data.jdbc.mapper;

import java.util.UUID;

import com.example.user.data.jdbc.model.UserJdbcEntity;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserJdbcMapperContract {

    UserJdbcEntity toNewEntity(UserRequest request);

    UserJdbcEntity toExistingEntity(UUID id, UserRequest request);

    UserResponse toResponse(UserJdbcEntity entity);
}
