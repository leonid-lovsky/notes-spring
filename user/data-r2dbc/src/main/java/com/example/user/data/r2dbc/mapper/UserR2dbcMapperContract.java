package com.example.user.data.r2dbc.mapper;

import java.util.UUID;

import com.example.user.data.r2dbc.model.UserR2dbcEntity;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserR2dbcMapperContract {

    UserR2dbcEntity toNewEntity(UserRequest request);

    UserR2dbcEntity toExistingEntity(UUID id, UserRequest request);

    UserResponse toResponse(UserR2dbcEntity entity);

}
