package com.example.note;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity(name = "note")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class NoteEntity {

    @Id @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String content;
}
