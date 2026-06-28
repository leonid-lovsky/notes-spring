package com.example.note.data.jpa.repository;

import java.util.UUID;

import com.example.note.data.jpa.entity.NoteEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteJpaRepository extends JpaRepository<NoteEntity, UUID> {

}
