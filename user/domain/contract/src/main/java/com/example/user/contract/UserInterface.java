package com.example.user.contract;

import java.util.UUID;

import com.example.user.domain.UserRequest;

public interface UserInterface {

    Object existsById(UUID id);

    Object add(UserRequest request);

    Object findAll();

    Object findById(UUID id);

    Object replace(UUID id, UserRequest request);

    Object merge(UUID id, UserRequest request);

    Object remove(UUID id);
}
