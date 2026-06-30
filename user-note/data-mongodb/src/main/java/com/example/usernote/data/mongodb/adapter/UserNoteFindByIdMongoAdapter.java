package com.example.usernote.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByIdMongoAdapter implements UserNoteFindByIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteFindByIdMongoAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public Optional<UserNoteResponse> findById(UUID id) {
        return this.userNoteMongoRepository.findById(id).map(this.userNoteMongoMapper::toResponse);
    }

}
