package com.example.note.data.jpa;

import com.example.note.domain.NoteRemovePort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class NoteRemovePortAdapter implements NoteRemovePort {

	private final NoteJpaRepository noteJpaRepository;

	NoteRemovePortAdapter(NoteJpaRepository noteJpaRepository) {
		this.noteJpaRepository = noteJpaRepository;
	}

	@Override
	public void remove(UUID id) {
		noteJpaRepository.deleteById(id);
	}

}
