package com.example.note.data.mongodb;

import com.example.note.domain.NoteExistsByIdPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class NoteExistsByIdPortAdapter implements NoteExistsByIdPort {

	private final NoteMongoRepository noteMongoRepository;

	NoteExistsByIdPortAdapter(NoteMongoRepository noteMongoRepository) {
		this.noteMongoRepository = noteMongoRepository;
	}

	@Override
	public boolean existsById(UUID id) {
		return noteMongoRepository.existsById(id);
	}

}
