package com.example.note.contract;

import java.util.UUID;

public record Note(
    UUID id,
    String content
) {
}
