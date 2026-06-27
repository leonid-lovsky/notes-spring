package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteFindByUserIdPort;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdPortAdapter implements UserNoteFindByUserIdPort {

	private final UserNoteMongoRepository userNoteMongoRepository;

	private final UserNoteMongoMapper userNoteMongoMapper;

	UserNoteFindByUserIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository,
			UserNoteMongoMapper userNoteMongoMapper) {
		this.userNoteMongoRepository = userNoteMongoRepository;
		this.userNoteMongoMapper = userNoteMongoMapper;
	}

	@Override
	public List<UserNoteResponse> findByUserId(UUID userId) {
		return userNoteMongoRepository.findByIdUserId(userId).stream().map(userNoteMongoMapper::toResponse).toList();
	}

}
