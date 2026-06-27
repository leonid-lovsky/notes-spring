package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByNoteIdPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
class UserNoteFindByNoteIdPortAdapter implements UserNoteFindByNoteIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteFindByNoteIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteJpaRepository.findByIdNoteId(noteId).stream()
                .map(e -> new UserNote(e.getId().getUserId(), e.getId().getNoteId(), e.getRole()))
                .toList();
    }
}
