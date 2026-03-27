package com.example.note.jpa;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceReplace;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class JpaNoteServiceReplace implements NoteServiceReplace {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public NotePayloadResponse replace(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest) {
        return null;
    }
}
