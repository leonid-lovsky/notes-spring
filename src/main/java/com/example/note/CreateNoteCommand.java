package com.example.note;

public record CreateNoteCommand(
    String title,
    String content
) {

}
