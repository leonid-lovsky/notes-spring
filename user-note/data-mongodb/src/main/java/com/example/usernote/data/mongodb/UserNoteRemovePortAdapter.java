package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteRemovePort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserNoteRemovePortAdapter implements UserNoteRemovePort {

	private final UserNoteMongoRepository userNoteMongoRepository;

	UserNoteRemovePortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
		this.userNoteMongoRepository = userNoteMongoRepository;
	}

	@Override
	public void remove(UUID userId, UUID noteId) {
		userNoteMongoRepository.deleteById(new UserNoteKey(userId, noteId));
	}

}
