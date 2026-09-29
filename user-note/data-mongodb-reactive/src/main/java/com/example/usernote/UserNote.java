package com.example.usernote;

import java.util.UUID;
import org.springframework.data.annotation.Id;

public record UserNote(@Id UUID id, UUID userId, UUID noteId) {}
