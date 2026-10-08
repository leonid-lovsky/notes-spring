package com.example.usernote;

public class UserNoteMapper {

    public UserNoteResponse toResponse(UserNote userNote) {
        return new UserNoteResponse(userNote.id(), userNote.userId(), userNote.noteId());
    }
}
