package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdAndNoteIdPortAdapter implements UserNoteFindByUserIdAndNoteIdPort {

	private final NamedParameterJdbcTemplate jdbc;

	private final UserNoteJdbcMapper userNoteJdbcMapper;

	UserNoteFindByUserIdAndNoteIdPortAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapper userNoteJdbcMapper) {
		this.jdbc = jdbc;
		this.userNoteJdbcMapper = userNoteJdbcMapper;
	}

	@Override
	public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
		return jdbc
			.query("SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
					Map.of("userId", userId, "noteId", noteId), userNoteJdbcMapper::fromRow)
			.stream()
			.findFirst();
	}

}
