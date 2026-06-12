package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteOutputPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteOutputAdapter implements UserNoteOutputPort {

    private final UserNoteRepository userNoteRepository;

    UserNoteOutputAdapter(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteRepository.existsById(new UserNoteId(userId, noteId));
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteRepository.findById(new UserNoteId(userId, noteId)).map(UserNoteOutputAdapter::toDomain);
    }

    @Override
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteRepository.findByIdUserId(userId).stream().map(UserNoteOutputAdapter::toDomain).toList();
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteRepository.findByIdNoteId(noteId).stream().map(UserNoteOutputAdapter::toDomain).toList();
    }

    @Override
    public void add(UserNote userNote) {
        userNoteRepository.save(toEntity(userNote));
    }

    @Override
    public void replace(UserNote userNote) {
        userNoteRepository.save(toEntity(userNote));
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        userNoteRepository.deleteById(new UserNoteId(userId, noteId));
    }

    private static UserNote toDomain(UserNoteEntity entity) {
        return new UserNote(entity.getId().getUserId(), entity.getId().getNoteId(), entity.getRole());
    }

    private static UserNoteEntity toEntity(UserNote userNote) {
        return new UserNoteEntity(new UserNoteId(userNote.userId(), userNote.noteId()), userNote.role());
    }
}
