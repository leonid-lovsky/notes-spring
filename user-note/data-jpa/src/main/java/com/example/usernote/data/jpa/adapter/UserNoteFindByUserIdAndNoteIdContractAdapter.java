package com.example.usernote.data.jpa.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdAndNoteIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteEntityMapperContract;
import com.example.usernote.data.jpa.model.UserNoteId;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdContractAdapter implements UserNoteFindByUserIdAndNoteIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteEntityMapperContract userNoteEntityMapper;

    UserNoteFindByUserIdAndNoteIdContractAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteEntityMapperContract userNoteEntityMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteEntityMapper = userNoteEntityMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.findById(new UserNoteId(userId, noteId))
            .map(this.userNoteEntityMapper::toResponse);
    }

}
