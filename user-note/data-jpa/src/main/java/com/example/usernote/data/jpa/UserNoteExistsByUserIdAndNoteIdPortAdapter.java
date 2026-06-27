package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserNoteExistsByUserIdAndNoteIdPortAdapter implements UserNoteExistsByUserIdAndNoteIdPort {

	private final UserNoteJpaRepository userNoteJpaRepository;

	UserNoteExistsByUserIdAndNoteIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
		this.userNoteJpaRepository = userNoteJpaRepository;
	}

	@Override
	public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
		return userNoteJpaRepository.existsById(new UserNoteId(userId, noteId));
	}

}
