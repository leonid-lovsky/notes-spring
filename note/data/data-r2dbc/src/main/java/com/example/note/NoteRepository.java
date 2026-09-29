package com.example.note;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NoteRepository extends ReactiveCrudRepository<Note, UUID> {}
