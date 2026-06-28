package com.example.usernote.data.jpa.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByNoteIdContract;
import com.example.usernote.data.jpa.mapper.UserNoteEntityMapperContract;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdContractAdapter implements UserNoteFindByNoteIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteEntityMapperContract userNoteEntityMapper;

    UserNoteFindByNoteIdContractAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteEntityMapperContract userNoteEntityMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteEntityMapper = userNoteEntityMapper;
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteJpaRepository.findByIdNoteId(noteId)
            .stream()
            .map(this.userNoteEntityMapper::toResponse)
            .toList();
    }

}
