package com.example.note.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties(NoteKafkaProperties.class)
public class NoteKafkaMessagingConfig {

    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    @ConditionalOnMissingBean(NoteKafkaMessagePublisher.class)
    NoteKafkaMessagePublisher kafkaNoteMessagePublisher(
        KafkaTemplate<String, String> kafkaTemplate,
        ObjectMapper objectMapper,
        NoteKafkaProperties properties
    ) {
        return new SpringKafkaNoteMessagePublisher(kafkaTemplate, objectMapper, properties);
    }

    @Bean
    @ConditionalOnMissingBean(NoteKafkaMessagePublisher.class)
    NoteKafkaMessagePublisher loggingNoteKafkaMessagePublisher() {
        return new LoggingNoteKafkaMessagePublisher();
    }
}
