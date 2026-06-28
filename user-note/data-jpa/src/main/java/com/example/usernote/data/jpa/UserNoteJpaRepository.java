package com.example.usernote.data.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserNoteJpaRepository extends JpaRepository<UserNoteEntity, UserNoteId> {

    List<UserNoteEntity> findByIdUserId(UUID userId);

    List<UserNoteEntity> findByIdNoteId(UUID noteId);

}
