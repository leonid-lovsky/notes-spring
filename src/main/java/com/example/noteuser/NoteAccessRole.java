package com.example.noteuser;

public enum NoteAccessRole {
    CREATOR,
    EDITOR,
    VIEWER;

    public boolean canRead() {
        return true;
    }

    public boolean canEdit() {
        return this == CREATOR || this == EDITOR;
    }

    public boolean canDelete() {
        return this == CREATOR;
    }
}
