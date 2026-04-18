package com.example;

import com.example.application.crud.jpa.EntityMapper;
import com.example.application.user.UserRequest;
import com.example.application.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapper extends EntityMapper<UserRequest, UserResponse, UserEntity> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
