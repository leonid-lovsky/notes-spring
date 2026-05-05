package com.example.note.runtime.spring;

import com.example.note.contract.NoteEventPublisher;
import com.example.note.messaging.kafka.NoteKafkaMessagePublisher;
import com.example.note.messaging.kafka.NoteKafkaRelay;
import com.example.note.contract.NoteService;
import com.example.note.contract.NoteStore;
import com.example.note.messaging.outbox.NoteOutboxMessageRepository;
import com.example.note.messaging.outbox.NoteOutboxPublisher;
import com.example.note.persistence.jpa.NoteJpaAdapter;
import com.example.note.persistence.jpa.NoteJpaMapper;
import com.example.note.persistence.jpa.NoteRepository;
import com.example.note.service.DefaultNoteService;
import com.example.note.service.NoteServiceMapper;
import com.example.note.service.NoteServiceMapperImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoteSpringConfig {

    @Bean
    NoteServiceMapper noteServiceMapper() {
        return new NoteServiceMapperImpl();
    }

    @Bean
    NoteStore noteStore(NoteRepository repository, NoteJpaMapper mapper) {
        return new NoteJpaAdapter(repository, mapper);
    }

    @Bean
    NoteEventPublisher noteEventPublisher(NoteOutboxMessageRepository repository) {
        return new NoteOutboxPublisher(repository);
    }

    @Bean
    NoteKafkaRelay noteKafkaRelay(
        NoteOutboxMessageRepository repository,
        NoteKafkaMessagePublisher publisher
    ) {
        return new NoteKafkaRelay(repository, publisher);
    }

    @Bean
    DefaultNoteService noteCoreService(
        NoteStore noteStore,
        NoteEventPublisher noteEventPublisher,
        NoteServiceMapper mapper
    ) {
        return new DefaultNoteService(noteStore, noteEventPublisher, mapper);
    }

    @Bean
    @Primary
    NoteService noteService(DefaultNoteService noteCoreService) {
        return new TransactionalNoteService(noteCoreService);
    }
}
