package com.example.usernote.data.jpa.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdAndNoteIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.model.UserNoteId;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdJpaAdapter implements UserNoteFindByUserIdAndNoteIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteFindByUserIdAndNoteIdJpaAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.findById(new UserNoteId(userId, noteId))
            .map(this.userNoteJpaMapper::toResponse);
    }

}
