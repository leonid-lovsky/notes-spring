package com.example.usernote.feign;

import com.example.usernote.domain.NoteClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class NoteFeignClientFallback implements NoteClient {

    @Override
    public Optional<NoteSummary> findById(UUID noteId) {
        return Optional.empty();
    }
}
