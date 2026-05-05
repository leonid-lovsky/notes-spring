package com.example.auth.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthStore {

    Auth create(Auth auth);

    List<Auth> readAll();

    Optional<Auth> readById(UUID id);

    Auth update(Auth auth);

    Auth replace(Auth auth);

    void deleteById(UUID id);
}
