package com.example.usernote.data.jpa.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdJpaAdapter implements UserNoteFindByUserIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteFindByUserIdJpaAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteJpaRepository.findByIdUserId(userId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

}
