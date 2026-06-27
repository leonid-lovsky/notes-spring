package com.example.note.data.jpa;

import com.example.note.domain.NoteExistsByIdPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class NoteExistsByIdPortAdapter implements NoteExistsByIdPort {

	private final NoteJpaRepository noteJpaRepository;

	NoteExistsByIdPortAdapter(NoteJpaRepository noteJpaRepository) {
		this.noteJpaRepository = noteJpaRepository;
	}

	@Override
	public boolean existsById(UUID id) {
		return noteJpaRepository.existsById(id);
	}

}
