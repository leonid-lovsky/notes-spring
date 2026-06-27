package com.example.note.data.jdbc;

import com.example.note.domain.NoteExistsByIdPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class NoteExistsByIdPortAdapter implements NoteExistsByIdPort {

	private final NamedParameterJdbcTemplate jdbc;

	NoteExistsByIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public boolean existsById(UUID id) {
		Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM notes WHERE id = :id", Map.of("id", id),
				Integer.class);
		return count != null && count > 0;
	}

}
