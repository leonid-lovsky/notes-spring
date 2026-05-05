package com.example.auth.persistence.jpa;

import com.example.auth.contract.Auth;
import com.example.auth.contract.AuthNotFoundException;
import com.example.auth.contract.AuthStore;
import com.example.crud.persistence.jpa.CrudJpaAdapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthJpaAdapter extends CrudJpaAdapter<Auth, AuthEntity, UUID> implements AuthStore {

    public AuthJpaAdapter(AuthRepository repository, AuthJpaMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public Auth create(Auth auth) {
        return super.create(auth);
    }

    @Override
    public List<Auth> readAll() {
        return super.readAll();
    }

    @Override
    public Optional<Auth> readById(UUID id) {
        return super.readById(id);
    }

    @Override
    public Auth update(Auth auth) {
        return super.update(auth);
    }

    @Override
    public Auth replace(Auth auth) {
        return super.replace(auth);
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected UUID getId(Auth auth) {
        return auth.id();
    }

    @Override
    protected RuntimeException newMissingModelException(UUID id) {
        return new AuthNotFoundException(id);
    }
}
