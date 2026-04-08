package com.example.user.jpa;

import com.example.crud.jpa.EntityMapper;
import com.example.user.UserRequest;
import com.example.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapper extends EntityMapper<UserRequest, UserResponse, UserEntity> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
