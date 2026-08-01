package com.example.note.data.mongodb.reactive.model;

import java.util.UUID;

import com.example.note.domain.NotePersistable;
import org.jspecify.annotations.NullUnmarked;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NullUnmarked
@Document(collection = "notes")
public class NoteReactiveDocument implements NotePersistable {

    @Id
    private UUID id;

    private String content;

    protected NoteReactiveDocument() {

    }

    public NoteReactiveDocument(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
