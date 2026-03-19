package com.example.note.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {

    List<NoteEntity> findAllByIdInOrderByUpdatedAtDesc(Collection<UUID> noteIds);
}
