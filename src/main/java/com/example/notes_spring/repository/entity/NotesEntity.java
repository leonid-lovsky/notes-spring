package com.example.notes_spring.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "notes")
public class NotesEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
