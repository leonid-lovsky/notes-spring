package com.example.usernote.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserNoteRepository extends JpaRepository<UserNoteEntity, UUID> {
}
