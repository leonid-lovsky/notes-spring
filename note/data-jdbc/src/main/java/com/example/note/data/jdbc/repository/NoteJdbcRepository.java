package com.example.note.data.jdbc.repository;

import java.util.UUID;

import com.example.note.data.jdbc.model.NoteJdbcEntity;

import org.springframework.data.repository.ListCrudRepository;

public interface NoteJdbcRepository extends ListCrudRepository<NoteJdbcEntity, UUID> {

}
