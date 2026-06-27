package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdAndNoteIdPortAdapter implements UserNoteFindByUserIdAndNoteIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteFindByUserIdAndNoteIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteJpaRepository.findById(new UserNoteId(userId, noteId))
                .map(e -> new UserNote(e.getId().getUserId(), e.getId().getNoteId(), e.getRole()));
    }
}
