package com.example.usernote.data.jpa.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteEntityMapperContract;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdContractAdapter implements UserNoteFindByUserIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteEntityMapperContract userNoteEntityMapper;

    UserNoteFindByUserIdContractAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteEntityMapperContract userNoteEntityMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteEntityMapper = userNoteEntityMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteJpaRepository.findByIdUserId(userId)
            .stream()
            .map(this.userNoteEntityMapper::toResponse)
            .toList();
    }

}
