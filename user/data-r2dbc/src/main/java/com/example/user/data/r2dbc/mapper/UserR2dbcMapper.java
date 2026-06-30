package com.example.user.data.r2dbc.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.user.data.r2dbc.model.UserR2dbcEntity;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Component;

@Component
class UserR2dbcMapper implements UserR2dbcMapperContract {

    @Override
    public UserR2dbcEntity toNewEntity(UserRequest request) {
        return new UserR2dbcEntity(request.username(), request.email());
    }

    @Override
    public UserR2dbcEntity toExistingEntity(UUID id, UserRequest request) {
        return new UserR2dbcEntity(id, request.username(), request.email());
    }

    @Override
    public UserResponse toResponse(UserR2dbcEntity entity) {
        return new UserResponse(Objects.requireNonNull(entity.getId()), entity.getUsername(), entity.getEmail());
    }

}
