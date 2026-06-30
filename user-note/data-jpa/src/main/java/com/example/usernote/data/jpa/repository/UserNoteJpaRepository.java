package com.example.usernote.data.jpa.repository;

import java.util.List;
import java.util.UUID;

import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.model.UserNoteId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNoteJpaRepository extends JpaRepository<UserNoteEntity, UserNoteId> {

    List<UserNoteEntity> findByIdUserId(UUID userId);

    List<UserNoteEntity> findByIdNoteId(UUID noteId);

}
