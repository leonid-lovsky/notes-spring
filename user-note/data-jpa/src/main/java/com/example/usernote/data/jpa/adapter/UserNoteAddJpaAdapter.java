package com.example.usernote.data.jpa.adapter;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddJpaAdapter implements UserNoteAddContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteAddJpaAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteEntity saved = this.userNoteJpaRepository.save(this.userNoteJpaMapper.toEntity(request));
        return this.userNoteJpaMapper.toResponse(saved);
    }

}
