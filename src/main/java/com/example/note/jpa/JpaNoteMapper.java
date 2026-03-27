package com.example.note.jpa;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import org.jspecify.annotations.Nullable;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface JpaNoteMapper {

    @Mapping(target = "id", ignore = true)
    JpaNoteEntity toJpaNoteEntity(@Nullable NotePayloadRequest notePayloadRequest);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNoteJpaEntity(@Nullable NotePayloadRequest notePayloadRequest, @MappingTarget @Nullable JpaNoteEntity jpaNoteEntity);

    @Mapping(target = "id", ignore = true)
    void replaceNoteEntity(@Nullable NotePayloadRequest notePayloadRequest, @MappingTarget @Nullable JpaNoteEntity jpaNoteEntity);

    NotePayloadResponse toNotePayloadResponse(@Nullable JpaNoteEntity jpaNoteEntity);
}
