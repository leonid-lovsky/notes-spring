package com.example.notes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity(name = "notes")
@Accessors(fluent = true, chain = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class NotesEntity {

    @Setter @Getter @Id @GeneratedValue
    private UUID id;

    @Setter @Getter @NonNull
    private String content;
}
