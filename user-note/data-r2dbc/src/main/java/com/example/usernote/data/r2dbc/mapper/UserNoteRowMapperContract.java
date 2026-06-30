package com.example.usernote.data.r2dbc.mapper;

import com.example.usernote.domain.UserNoteResponse;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public interface UserNoteRowMapperContract {

    UserNoteResponse fromRow(Row row, RowMetadata rowMetadata);

}
