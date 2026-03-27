package com.example.note.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface JpaNoteRepository extends JpaRepository<JpaNoteEntity, UUID> {

}
