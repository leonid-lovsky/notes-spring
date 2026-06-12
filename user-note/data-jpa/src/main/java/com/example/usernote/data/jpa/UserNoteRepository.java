package com.example.usernote.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface UserNoteRepository extends JpaRepository<UserNoteEntity, UserNoteId> {

    List<UserNoteEntity> findByIdUserId(java.util.UUID userId);

    List<UserNoteEntity> findByIdNoteId(java.util.UUID noteId);
}
