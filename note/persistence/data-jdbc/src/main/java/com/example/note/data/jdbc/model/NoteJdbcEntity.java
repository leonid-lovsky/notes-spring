package com.example.note.data.jdbc.model;

import java.util.UUID;

import com.example.note.domain.NotePersistable;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NullUnmarked
@Table("notes")
public class NoteJdbcEntity implements NotePersistable {

    @Id
    private @Nullable UUID id;

    @Column("content")
    private String content;

    protected NoteJdbcEntity() {

    }

    public NoteJdbcEntity(String content) {
        this.content = content;
    }

    public NoteJdbcEntity(UUID id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public @Nullable UUID getId() {
        return this.id;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
