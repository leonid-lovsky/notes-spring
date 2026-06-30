package com.example.usernote.data.jpa.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByIdJpaAdapter implements UserNoteFindByIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteFindByIdJpaAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public Optional<UserNoteResponse> findById(UUID id) {
        return this.userNoteJpaRepository.findById(id).map(this.userNoteJpaMapper::toResponse);
    }

}
