package com.example.user.data.jpa;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import java.util.UUID;

interface UserJpaMapper {

    UserEntity toNewEntity(UserRequest request);

    UserEntity toExistingEntity(UUID id, UserRequest request);

    UserResponse toResponse(UserEntity entity);

}
