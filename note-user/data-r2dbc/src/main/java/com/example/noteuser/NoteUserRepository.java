package com.example.noteuser;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NoteUserRepository extends ReactiveCrudRepository<NoteUser, UUID> {}
