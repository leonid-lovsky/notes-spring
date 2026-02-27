package com.example.notes.payload;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data @Builder
public class NotesResponse {

    private final UUID id;
    private final String content;
}
