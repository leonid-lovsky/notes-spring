package com.example.user.jpa;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import org.jspecify.annotations.Nullable;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface JpaUserMapper {

    @Mapping(target = "id", ignore = true)
    JpaUserEntity toJpaUserEntity(@Nullable UserPayloadRequest userPayloadRequest);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserJpaEntity(@Nullable UserPayloadRequest userPayloadRequest, @MappingTarget @Nullable JpaUserEntity jpaUserEntity);

    @Mapping(target = "id", ignore = true)
    void replaceUserJpaEntity(@Nullable UserPayloadRequest userPayloadRequest, @MappingTarget @Nullable JpaUserEntity jpaUserEntity);

    UserPayloadResponse toUserPayloadResponse(@Nullable JpaUserEntity jpaUserEntity);
}
