package com.example.noteuser;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface NoteUserRepository extends ListCrudRepository<NoteUser, UUID> {}
