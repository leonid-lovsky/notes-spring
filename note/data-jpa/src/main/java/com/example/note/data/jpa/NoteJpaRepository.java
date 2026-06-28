package com.example.note.data.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

interface NoteJpaRepository extends JpaRepository<NoteEntity, UUID> {

}
