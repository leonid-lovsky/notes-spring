package com.example.note.messaging.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoteOutboxMessageRepository extends JpaRepository<NoteOutboxMessageEntity, UUID> {

    List<NoteOutboxMessageEntity> findAllByOrderByCreatedAtAsc();
}
