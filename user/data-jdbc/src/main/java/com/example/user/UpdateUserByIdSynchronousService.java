package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserByIdSynchronousService implements UpdateUserByIdSynchronous {

    @Override
    public UserResponse execute(UUID id, UserRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
