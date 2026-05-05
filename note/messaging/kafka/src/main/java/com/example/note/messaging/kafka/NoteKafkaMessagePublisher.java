package com.example.note.messaging.kafka;

public interface NoteKafkaMessagePublisher {

    void publish(NoteKafkaMessage message);
}
