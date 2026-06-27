package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdAndNoteIdPortAdapter implements UserNoteFindByUserIdAndNoteIdPort {

    private final UserNoteMongoRepository userNoteMongoRepository;
    private final UserNoteMongoMapper userNoteMongoMapper;

    UserNoteFindByUserIdAndNoteIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository,
                                             UserNoteMongoMapper userNoteMongoMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteMongoRepository.findById(new UserNoteKey(userId, noteId))
                .map(userNoteMongoMapper::toResponse);
    }
}
