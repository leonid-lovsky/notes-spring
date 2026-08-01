package com.example.usernote.data.jdbc.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.data.jdbc.model.UserNoteJdbcEntity;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteJdbcRepository extends ListCrudRepository<UserNoteJdbcEntity, UUID> {

    List<UserNoteJdbcEntity> findByUserId(UUID userId);

    List<UserNoteJdbcEntity> findByNoteId(UUID noteId);

    Optional<UserNoteJdbcEntity> findByUserIdAndNoteId(UUID userId, UUID noteId);

    boolean existsByUserId(UUID userId);

    boolean existsByNoteId(UUID noteId);

    boolean existsByUserIdAndNoteId(UUID userId, UUID noteId);
}
