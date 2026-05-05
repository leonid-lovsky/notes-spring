package com.example.user.persistence.jpa;

import com.example.crud.persistence.jpa.CrudEntityMapper;
import com.example.user.contract.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserJpaMapper extends CrudEntityMapper<User, UserEntity> {
}
