package com.example.usernote.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByNoteIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdMongoAdapter implements UserNoteFindByNoteIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteFindByNoteIdMongoAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoRepository.findByNoteId(noteId)
            .stream()
            .map(this.userNoteMongoMapper::toResponse)
            .toList();
    }

}
