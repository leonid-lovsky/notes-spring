package com.example.usernote.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface UserNoteJpaRepository extends JpaRepository<UserNoteEntity, UserNoteId> {

    List<UserNoteEntity> findByIdUserId(UUID userId);

    List<UserNoteEntity> findByIdNoteId(UUID noteId);

}
