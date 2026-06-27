package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNoteFindByUserIdPort;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdPortAdapter implements UserNoteFindByUserIdPort {

	private final NamedParameterJdbcTemplate jdbc;

	private final UserNoteJdbcMapper userNoteJdbcMapper;

	UserNoteFindByUserIdPortAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapper userNoteJdbcMapper) {
		this.jdbc = jdbc;
		this.userNoteJdbcMapper = userNoteJdbcMapper;
	}

	@Override
	public List<UserNoteResponse> findByUserId(UUID userId) {
		return jdbc.query("SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId",
				Map.of("userId", userId), userNoteJdbcMapper::fromRow);
	}

}
