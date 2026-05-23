package com.example.note.data.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notes")
class NoteEntity {

    @Id
    UUID id;
    String ownerId;
    String title;
    @Column(length = 65535)
    String content;
    Instant createdAt;

    protected NoteEntity() {}
}
