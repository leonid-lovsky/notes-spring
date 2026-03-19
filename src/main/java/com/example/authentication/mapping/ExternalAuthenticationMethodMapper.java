package com.example.authentication.mapping;

import com.example.authentication.ExternalAuthenticationMethodView;
import com.example.authentication.LinkExternalAuthenticationCommand;
import com.example.authentication.persistence.ExternalAuthenticationMethodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExternalAuthenticationMethodMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "linkedAt", ignore = true)
    ExternalAuthenticationMethodEntity commandToEntity(LinkExternalAuthenticationCommand command);

    ExternalAuthenticationMethodView toView(ExternalAuthenticationMethodEntity entity);
}
