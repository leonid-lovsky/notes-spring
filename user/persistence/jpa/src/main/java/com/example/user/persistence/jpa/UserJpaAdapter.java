package com.example.user.persistence.jpa;

import com.example.crud.persistence.jpa.CrudJpaAdapter;
import com.example.user.contract.User;
import com.example.user.contract.UserNotFoundException;
import com.example.user.contract.UserStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserJpaAdapter extends CrudJpaAdapter<User, UserEntity, UUID> implements UserStore {

    public UserJpaAdapter(UserRepository repository, UserJpaMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public User create(User user) {
        return super.create(user);
    }

    @Override
    public List<User> readAll() {
        return super.readAll();
    }

    @Override
    public Optional<User> readById(UUID id) {
        return super.readById(id);
    }

    @Override
    public User update(User user) {
        return super.update(user);
    }

    @Override
    public User replace(User user) {
        return super.replace(user);
    }

    @Override
    public void deleteById(UUID id) {
        super.deleteById(id);
    }

    @Override
    protected UUID getId(User user) {
        return user.id();
    }

    @Override
    protected RuntimeException newMissingModelException(UUID id) {
        return new UserNotFoundException(id);
    }
}
