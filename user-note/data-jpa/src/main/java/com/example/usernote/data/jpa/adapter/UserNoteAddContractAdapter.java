package com.example.usernote.data.jpa.adapter;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.data.jpa.mapper.UserNoteEntityMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddContractAdapter implements UserNoteAddContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteEntityMapperContract userNoteEntityMapper;

    UserNoteAddContractAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteEntityMapperContract userNoteEntityMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteEntityMapper = userNoteEntityMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteEntity saved = this.userNoteJpaRepository.save(this.userNoteEntityMapper.toEntity(request));
        return this.userNoteEntityMapper.toResponse(saved);
    }

}
