package com.example;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper<Request, Response> extends CrudMapper<Request, Response, NoteEntity> {

}
