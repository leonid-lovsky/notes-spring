package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface UserNoteRepository extends ListCrudRepository<UserNote, UUID> {}
