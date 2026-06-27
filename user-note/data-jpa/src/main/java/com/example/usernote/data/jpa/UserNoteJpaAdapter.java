package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class UserNoteJpaAdapter implements UserNoteRepository {

    private final UserNoteJpaRepository userNoteJpaRepository;
    private final EntityManager em;

    UserNoteJpaAdapter(UserNoteJpaRepository userNoteJpaRepository, EntityManager em) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.em = em;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteJpaRepository.existsById(new UserNoteId(userId, noteId));
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteJpaRepository.findById(new UserNoteId(userId, noteId))
                .map(UserNoteJpaAdapter::toDomain);
    }

    @Override
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteJpaRepository.findByIdUserId(userId).stream()
                .map(UserNoteJpaAdapter::toDomain)
                .toList();
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteJpaRepository.findByIdNoteId(noteId).stream()
                .map(UserNoteJpaAdapter::toDomain)
                .toList();
    }

    @Override
    public UserNote add(UserNote userNote) {
        em.persist(toEntity(userNote));
        return userNote;
    }

    @Override
    public void replace(UserNote userNote) {
        userNoteJpaRepository.save(toEntity(userNote));
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        userNoteJpaRepository.deleteById(new UserNoteId(userId, noteId));
    }

    private static UserNote toDomain(UserNoteEntity entity) {
        return new UserNote(entity.getId().getUserId(), entity.getId().getNoteId(), entity.getRole());
    }

    private static UserNoteEntity toEntity(UserNote userNote) {
        return new UserNoteEntity(new UserNoteId(userNote.userId(), userNote.noteId()), userNote.role());
    }
}
