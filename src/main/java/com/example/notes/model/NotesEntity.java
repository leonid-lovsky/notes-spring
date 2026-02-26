package com.example.notes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity(name = "notes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class NotesEntity {

    @Getter
    @Setter
    @Id @GeneratedValue
    private UUID id;

    @Getter
    @Setter
    @NotNull
    private String content;
}
