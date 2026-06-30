package com.example.usernote.data.jpa.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByNoteIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdJpaAdapter implements UserNoteFindByNoteIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteFindByNoteIdJpaAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteJpaMapperContract userNoteJpaMapper) {
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
