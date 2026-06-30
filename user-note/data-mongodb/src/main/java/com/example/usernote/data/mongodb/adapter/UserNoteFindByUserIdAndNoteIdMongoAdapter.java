package com.example.usernote.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdAndNoteIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteKey;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdMongoAdapter implements UserNoteFindByUserIdAndNoteIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteFindByUserIdAndNoteIdMongoAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.findById(new UserNoteKey(userId, noteId))
            .map(this.userNoteMongoMapper::toResponse);
    }

}
