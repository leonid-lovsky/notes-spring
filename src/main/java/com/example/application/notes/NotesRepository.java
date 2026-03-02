package com.example.application.notes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface NotesRepository extends JpaRepository<NotesEntity, UUID> {

}