package com.example.notes.payload;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class NotesPayload {

    private final String content;
}
