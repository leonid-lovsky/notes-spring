package com.example.notes.repository;

import com.example.notes.model.NotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotesRepository extends JpaRepository<NotesEntity, UUID> {

}
