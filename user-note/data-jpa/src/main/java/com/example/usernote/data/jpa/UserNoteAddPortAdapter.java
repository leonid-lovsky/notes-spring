package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteAddPort;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddPortAdapter implements UserNoteAddPort {

    private final UserNoteJpaRepository userNoteJpaRepository;
    private final UserNoteJpaMapper userNoteJpaMapper;

    UserNoteAddPortAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapper userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteEntity saved = userNoteJpaRepository.save(userNoteJpaMapper.toEntity(request));
        return userNoteJpaMapper.toResponse(saved);
    }
}
