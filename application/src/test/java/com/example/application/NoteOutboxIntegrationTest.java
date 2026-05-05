package com.example.application;

import com.example.note.contract.CreateNoteRequest;
import com.example.note.contract.NoteResponse;
import com.example.note.contract.NoteService;
import com.example.note.contract.NoteEventType;
import com.example.note.messaging.outbox.NoteOutboxMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NoteOutboxIntegrationTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteOutboxMessageRepository outboxRepository;

    @Test
    void createWritesOutboxMessageInSameApplicationFlow() {
        outboxRepository.deleteAll();

        NoteResponse created = noteService.create(new CreateNoteRequest("outbox note"));

        assertThat(created.id()).isNotNull();
        assertThat(outboxRepository.findAll())
            .singleElement()
            .satisfies(message -> {
                assertThat(message.getNoteId()).isEqualTo(created.id());
                assertThat(message.getType()).isEqualTo(NoteEventType.CREATED);
                assertThat(message.getPayload()).isEqualTo("outbox note");
                assertThat(message.getCreatedAt()).isNotNull();
            });
    }
}
