package com.example.note.data.jpa;

import com.example.note.domain.NoteFindAllPort;
import com.example.note.domain.NoteResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

	private final NoteJpaRepository noteJpaRepository;

	private final NoteJpaMapper noteJpaMapper;

	NoteFindAllPortAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapper noteJpaMapper) {
		this.noteJpaRepository = noteJpaRepository;
		this.noteJpaMapper = noteJpaMapper;
	}

	@Override
	public List<NoteResponse> findAll() {
		return noteJpaRepository.findAll().stream().map(noteJpaMapper::toResponse).toList();
	}

}
