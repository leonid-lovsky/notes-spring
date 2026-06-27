package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNoteReplacePort;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class UserNoteReplacePortAdapter implements UserNoteReplacePort {

	private final NamedParameterJdbcTemplate jdbc;

	UserNoteReplacePortAdapter(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
		jdbc.update("UPDATE user_notes SET role = :role WHERE user_id = :userId AND note_id = :noteId",
				Map.of("userId", userId, "noteId", noteId, "role", request.role().name()));
		return new UserNoteResponse(userId, noteId, request.role());
	}

}
