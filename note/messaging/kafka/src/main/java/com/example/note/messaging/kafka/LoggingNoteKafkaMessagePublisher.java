package com.example.note.messaging.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNoteKafkaMessagePublisher implements NoteKafkaMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingNoteKafkaMessagePublisher.class);

    @Override
    public void publish(NoteKafkaMessage message) {
        log.info(
            "Published note event to kafka relay: outboxId={}, noteId={}, type={}, payload={}",
            message.outboxId(),
            message.noteId(),
            message.type(),
            message.payload()
        );
    }
}
