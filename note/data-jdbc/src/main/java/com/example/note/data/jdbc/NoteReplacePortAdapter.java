package com.example.note.data.jdbc;

import com.example.note.domain.NoteReplacePort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class NoteReplacePortAdapter implements NoteReplacePort {

	private final NamedParameterJdbcTemplate jdbc;

	NoteReplacePortAdapter(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public NoteResponse replace(UUID id, NoteRequest request) {
		jdbc.update("UPDATE notes SET content = :content WHERE id = :id",
				Map.of("id", id, "content", request.content()));
		return new NoteResponse(id, request.content());
	}

}
