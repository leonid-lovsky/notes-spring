package com.example.user.data.jpa;

import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

interface UserJpaMapper {

    UserEntity toNewEntity(UserRequest request);

    UserEntity toExistingEntity(UUID id, UserRequest request);

    UserResponse toResponse(UserEntity entity);

}
