package com.example.usernote.data.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.data.jpa.model.UserNoteEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteJpaRepository extends JpaRepository<UserNoteEntity, UUID> {

    List<UserNoteEntity> findByUserId(UUID userId);

    List<UserNoteEntity> findByNoteId(UUID noteId);

    Optional<UserNoteEntity> findByUserIdAndNoteId(UUID userId, UUID noteId);

    boolean existsByUserId(UUID userId);

    boolean existsByNoteId(UUID noteId);

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);
}
