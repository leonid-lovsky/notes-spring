package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByUserIdPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdPortAdapter implements UserNoteFindByUserIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteFindByUserIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteJpaRepository.findByIdUserId(userId).stream()
                .map(e -> new UserNote(e.getId().getUserId(), e.getId().getNoteId(), e.getRole()))
                .toList();
    }
}
