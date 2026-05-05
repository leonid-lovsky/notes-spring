package com.example.note.contract;

public interface NoteEventPublisher {

    void publish(NoteEvent event);
}
