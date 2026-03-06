package com.example.notes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity(name = "notes")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
class NotesEntity {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String content;
}
