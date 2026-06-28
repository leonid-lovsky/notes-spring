package com.example.usernote.data.jpa;

import java.util.List;
import java.util.UUID;

import com.example.usernote.domain.UserNoteFindByNoteIdPort;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdPortAdapter implements UserNoteFindByNoteIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapper userNoteJpaMapper;

    UserNoteFindByNoteIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapper userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteJpaRepository.findByIdNoteId(noteId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

}
