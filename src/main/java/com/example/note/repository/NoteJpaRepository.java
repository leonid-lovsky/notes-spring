package com.example.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface NoteJpaRepository extends JpaRepository<NoteJpaEntity, UUID> {

}
