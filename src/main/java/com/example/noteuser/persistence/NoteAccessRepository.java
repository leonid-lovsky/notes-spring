package com.example.noteuser.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteAccessRepository extends JpaRepository<NoteAccessEntity, UUID> {

    List<NoteAccessEntity> findAllByUserId(UUID userId);

    Optional<NoteAccessEntity> findByNoteIdAndUserId(UUID noteId, UUID userId);

    void deleteAllByNoteId(UUID noteId);
}
