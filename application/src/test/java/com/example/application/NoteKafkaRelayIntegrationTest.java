package com.example.application;

import com.example.note.contract.CreateNoteRequest;
import com.example.note.contract.NoteService;
import com.example.note.messaging.kafka.NoteKafkaMessage;
import com.example.note.messaging.kafka.NoteKafkaMessagePublisher;
import com.example.note.messaging.kafka.NoteKafkaRelay;
import com.example.note.messaging.outbox.NoteOutboxMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = NoteKafkaRelayIntegrationTest.TestConfig.class)
class NoteKafkaRelayIntegrationTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteKafkaRelay relay;

    @Autowired
    private NoteOutboxMessageRepository outboxRepository;

    @Autowired
    private TestNoteKafkaMessagePublisher publisher;

    @Test
    void relayPublishesOutboxMessagesAndClearsOutbox() {
        outboxRepository.deleteAll();
        publisher.messages.clear();

        noteService.create(new CreateNoteRequest("relay note"));

        assertThat(outboxRepository.findAll()).hasSize(1);

        int relayedCount = relay.relayPendingMessages();

        assertThat(relayedCount).isEqualTo(1);
        assertThat(outboxRepository.findAll()).isEmpty();
        assertThat(publisher.messages)
            .singleElement()
            .satisfies(message -> assertThat(message.payload()).isEqualTo("relay note"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        TestNoteKafkaMessagePublisher testNoteKafkaMessagePublisher() {
            return new TestNoteKafkaMessagePublisher();
        }
    }

    static class TestNoteKafkaMessagePublisher implements NoteKafkaMessagePublisher {

        private final List<NoteKafkaMessage> messages = new ArrayList<>();

        @Override
        public void publish(NoteKafkaMessage message) {
            messages.add(message);
        }
    }
}
