package com.example.user.data.jdbc.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.user.data.jdbc.model.UserJdbcEntity;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Component;

@Component
class UserJdbcMapper implements UserJdbcMapperContract {

    @Override
    public UserJdbcEntity toNewEntity(UserRequest request) {
        return new UserJdbcEntity(request.username(), request.email());
    }

    @Override
    public UserJdbcEntity toExistingEntity(UUID id, UserRequest request) {
        return new UserJdbcEntity(id, request.username(), request.email());
    }

    @Override
    public UserResponse toResponse(UserJdbcEntity entity) {
        return new UserResponse(Objects.requireNonNull(entity.getId()), entity.getUsername(), entity.getEmail());
    }
}
