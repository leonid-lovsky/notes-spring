package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FindUserByIdSynchronousService implements FindUserByIdSynchronous {

    @Override
    public UserResponse execute(UUID id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
