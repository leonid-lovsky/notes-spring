package com.example.auth.persistence.jpa;

import com.example.auth.contract.Auth;
import com.example.crud.persistence.jpa.CrudEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthJpaMapper extends CrudEntityMapper<Auth, AuthEntity> {
}
