package com.example.notes;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "content", nullable = false)
    private String content;

    protected Note() {

    }

    public Note(String content) {
        this.uuid = UUID.randomUUID();
        this.content = content;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getContent() {
        return content;
    }
}
