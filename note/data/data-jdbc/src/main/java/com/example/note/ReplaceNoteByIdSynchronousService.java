package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReplaceNoteByIdSynchronousService implements ReplaceNoteByIdSynchronous {

    @Override
    public NoteResponse execute(UUID id, NoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
