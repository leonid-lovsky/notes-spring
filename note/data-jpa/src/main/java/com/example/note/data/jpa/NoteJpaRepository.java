package com.example.note.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface NoteJpaRepository extends JpaRepository<NoteEntity, UUID> {

}
