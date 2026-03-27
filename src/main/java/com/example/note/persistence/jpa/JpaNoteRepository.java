package com.example.note.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaNoteRepository extends JpaRepository<JpaNoteEntity, UUID> {

}
