package com.example.usernote;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserNoteByIdSynchronousService implements UpdateUserNoteByIdSynchronous {

    @Override
    public UserNoteResponse execute(UUID id, UserNoteRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
