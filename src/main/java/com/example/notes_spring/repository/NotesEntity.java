package com.example.notes_spring.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "notes")
public class NotesEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
}
