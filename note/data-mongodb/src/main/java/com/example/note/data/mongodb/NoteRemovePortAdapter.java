package com.example.note.data.mongodb;

import com.example.note.domain.NoteRemovePort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class NoteRemovePortAdapter implements NoteRemovePort {

	private final NoteMongoRepository noteMongoRepository;

	NoteRemovePortAdapter(NoteMongoRepository noteMongoRepository) {
		this.noteMongoRepository = noteMongoRepository;
	}

	@Override
	public void remove(UUID id) {
		noteMongoRepository.deleteById(id);
	}

}
