package com.example.usernote;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FindUserNoteByIdSynchronousService implements FindUserNoteByIdSynchronous {

    @Override
    public UserNoteResponse execute(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
