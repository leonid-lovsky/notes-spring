package com.example.note.persistence.jpa;

import com.example.note.application.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Repository
@Validated
@Transactional
@RequiredArgsConstructor
class JpaNoteRepositoryAdapter implements NoteRepository {

    private final JpaNoteRepository jpaNoteRepository;
}
