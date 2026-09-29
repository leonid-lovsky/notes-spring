package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReplaceUserByIdSynchronousService implements ReplaceUserByIdSynchronous {

    @Override
    public UserResponse execute(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
