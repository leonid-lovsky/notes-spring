package com.example.notes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity(name = "notes")
@Builder @NoArgsConstructor @AllArgsConstructor
public class NotesEntity {

    @Getter @Setter @Id @GeneratedValue
    private UUID id;

    @Getter @Setter @NotNull
    @Column(nullable = false)
    private String content;
}
