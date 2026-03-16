package com.example.note;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class NoteEntity {

    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    private String content;
}
