package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface UserNoteJDBCRepository extends ListCrudRepository<UserNote, UUID> {}
