package com.example.user.data.jpa.mapper;

import java.util.UUID;

import com.example.user.data.jpa.entity.UserEntity;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserEntityMapperContract {

    UserEntity toNewEntity(UserRequest request);

    UserEntity toExistingEntity(UUID id, UserRequest request);

    UserResponse toResponse(UserEntity entity);

}
