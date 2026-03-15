package com.example.note;

import com.example.notesusers.NoteUserEntity;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "note")
@Table(name = "note")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL)
    private Set<NoteUserEntity> noteUsers;

    public NoteEntity() {
    }

    UUID getId() {
        return id;
    }

    String getContent() {
        return content;
    }

    NoteEntity setContent(String content) {
        this.content = content;
        return this;
    }

    Set<NoteUserEntity> getNoteUsers() {
        return noteUsers;
    }
}
