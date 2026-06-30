package com.example.usernote.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdMongoAdapter implements UserNoteFindByUserIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteFindByUserIdMongoAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoRepository.findByIdUserId(userId)
            .stream()
            .map(this.userNoteMongoMapper::toResponse)
            .toList();
    }

}
