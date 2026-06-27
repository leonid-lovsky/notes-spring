package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserNoteExistsByUserIdAndNoteIdPortAdapter implements UserNoteExistsByUserIdAndNoteIdPort {

	private final UserNoteMongoRepository userNoteMongoRepository;

	UserNoteExistsByUserIdAndNoteIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
		this.userNoteMongoRepository = userNoteMongoRepository;
	}

	@Override
	public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
		return userNoteMongoRepository.existsById(new UserNoteKey(userId, noteId));
	}

}
