package com.example.note;

public record UpdateNoteCommand(
    String title,
    String content
) {

}
