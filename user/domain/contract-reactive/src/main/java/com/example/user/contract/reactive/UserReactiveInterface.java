package com.example.user.contract.reactive;

import java.util.UUID;

import com.example.user.domain.UserRequest;

public interface UserReactiveInterface<B, S, L, V> {

    B existsById(UUID id);

    S add(UserRequest request);

    L findAll();

    S findById(UUID id);

    S replace(UUID id, UserRequest request);

    S merge(UUID id, UserRequest request);

    V remove(UUID id);
}
