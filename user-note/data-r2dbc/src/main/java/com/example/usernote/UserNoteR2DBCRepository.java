package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserNoteR2DBCRepository extends ReactiveCrudRepository<UserNote, UUID> {}
