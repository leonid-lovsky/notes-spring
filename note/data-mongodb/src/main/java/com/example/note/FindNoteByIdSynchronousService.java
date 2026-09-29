package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FindNoteByIdSynchronousService implements FindNoteByIdSynchronous {

    @Override
    public NoteResponse execute(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
