package com.example.note.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

public class SpringKafkaNoteMessagePublisher implements NoteKafkaMessagePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final NoteKafkaProperties properties;

    public SpringKafkaNoteMessagePublisher(
        KafkaTemplate<String, String> kafkaTemplate,
        ObjectMapper objectMapper,
        NoteKafkaProperties properties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    public void publish(NoteKafkaMessage message) {
        kafkaTemplate.send(properties.getTopic(), message.noteId().toString(), serialize(message));
    }

    private String serialize(NoteKafkaMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize note kafka message", exception);
        }
    }
}
