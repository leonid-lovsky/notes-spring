package com.example.user.contract;

import java.util.UUID;

import com.example.user.domain.UserRequest;

public interface UserInterface<B, S, L> {

    B existsById(UUID id);

    S add(UserRequest request);

    L findAll();

    S findById(UUID id);

    S replace(UUID id, UserRequest request);

    S merge(UUID id, UserRequest request);

    S remove(UUID id);
}
