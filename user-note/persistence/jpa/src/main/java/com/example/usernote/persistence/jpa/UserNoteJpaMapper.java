package com.example.usernote.persistence.jpa;

import com.example.crud.persistence.jpa.CrudEntityMapper;
import com.example.usernote.contract.UserNote;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserNoteJpaMapper extends CrudEntityMapper<UserNote, UserNoteEntity> {
}
