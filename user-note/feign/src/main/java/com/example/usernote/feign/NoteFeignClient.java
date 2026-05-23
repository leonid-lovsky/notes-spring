package com.example.usernote.feign;

import com.example.usernote.domain.NoteClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "note-service", fallback = NoteFeignClientFallback.class)
public interface NoteFeignClient extends NoteClient {

    @GetMapping("/notes/{id}")
    @Override
    Optional<NoteSummary> findById(@PathVariable UUID id);
}
