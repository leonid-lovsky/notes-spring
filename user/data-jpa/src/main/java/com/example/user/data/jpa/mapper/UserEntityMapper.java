package com.example.user.data.jpa.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.user.data.jpa.model.UserEntity;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Component;

@Component
class UserEntityMapper implements UserEntityMapperContract {

    @Override
    public UserEntity toNewEntity(UserRequest request) {
        return new UserEntity(request.username(), request.email());
    }

    @Override
    public UserEntity toExistingEntity(UUID id, UserRequest request) {
        return new UserEntity(id, request.username(), request.email());
    }

    @Override
    public UserResponse toResponse(UserEntity entity) {
        return new UserResponse(Objects.requireNonNull(entity.getId()), entity.getUsername(), entity.getEmail());
    }

}
