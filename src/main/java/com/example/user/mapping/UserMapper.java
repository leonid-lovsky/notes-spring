package com.example.user.mapping;

import com.example.user.UserView;
import com.example.user.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserView toView(UserEntity entity);
}
