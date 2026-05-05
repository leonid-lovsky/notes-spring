package com.example.note.messaging.kafka;

import com.example.note.messaging.outbox.NoteOutboxMessageEntity;
import com.example.note.messaging.outbox.NoteOutboxMessageRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class NoteKafkaRelay {

    private final NoteOutboxMessageRepository outboxRepository;
    private final NoteKafkaMessagePublisher publisher;

    public NoteKafkaRelay(NoteOutboxMessageRepository outboxRepository, NoteKafkaMessagePublisher publisher) {
        this.outboxRepository = outboxRepository;
        this.publisher = publisher;
    }

    public int relayPendingMessages() {
        int relayedCount = 0;

        for (NoteOutboxMessageEntity message : outboxRepository.findAllByOrderByCreatedAtAsc()) {
            publisher.publish(toKafkaMessage(message));
            outboxRepository.delete(message);
            relayedCount++;
        }

        return relayedCount;
    }

    private NoteKafkaMessage toKafkaMessage(NoteOutboxMessageEntity message) {
        return new NoteKafkaMessage(
            message.getId(),
            message.getNoteId(),
            message.getType(),
            message.getPayload(),
            message.getCreatedAt()
        );
    }
}
