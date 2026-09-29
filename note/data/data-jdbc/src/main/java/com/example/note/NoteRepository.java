package com.example.note;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface NoteRepository extends ListCrudRepository<Note, UUID> {}
