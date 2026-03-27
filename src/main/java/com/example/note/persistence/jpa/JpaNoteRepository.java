package com.example.note.persistence.jpa;

import com.example.note.application.service.NoteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaNoteRepository extends JpaRepository<JpaNoteEntity, UUID>, NoteRepository {

}
