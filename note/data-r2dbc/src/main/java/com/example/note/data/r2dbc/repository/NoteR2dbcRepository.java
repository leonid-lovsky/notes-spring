package com.example.note.data.r2dbc.repository;

import java.util.UUID;

import com.example.note.data.r2dbc.model.NoteR2dbcEntity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NoteR2dbcRepository extends ReactiveCrudRepository<NoteR2dbcEntity, UUID> {

}
