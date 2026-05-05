package com.example.note.messaging.outbox;

import com.example.note.contract.NoteEvent;
import com.example.note.contract.NoteEventPublisher;

import java.time.Instant;

public class NoteOutboxPublisher implements NoteEventPublisher {

    private final NoteOutboxMessageRepository repository;

    public NoteOutboxPublisher(NoteOutboxMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void publish(NoteEvent event) {
        NoteOutboxMessageEntity message = new NoteOutboxMessageEntity();
        message.setNoteId(event.noteId());
        message.setType(event.type());
        message.setPayload(event.content());
        message.setCreatedAt(Instant.now());
        repository.save(message);
    }
}
