package com.example.note.presentation.rest;

import com.example.note.RequestNotePayload;
import com.example.note.ResponseNotePayload;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface RestNoteMapper {

    RequestNotePayload toRequestNotePayload(RequestNoteBody requestNoteBody);

    ResponseNoteBody toResponseNoteBody(ResponseNotePayload responseNotePayload);
}
