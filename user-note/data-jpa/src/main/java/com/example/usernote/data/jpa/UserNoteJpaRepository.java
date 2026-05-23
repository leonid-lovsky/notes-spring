package com.example.usernote.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface UserNoteJpaRepository extends JpaRepository<UserNoteEntity, UUID> {
    List<UserNoteEntity> findByUserId(String userId);
    List<UserNoteEntity> findByOwnerId(String ownerId);
    Optional<UserNoteEntity> findByUserIdAndNoteId(String userId, UUID noteId);
    void deleteByUserIdAndNoteId(String userId, UUID noteId);
}
