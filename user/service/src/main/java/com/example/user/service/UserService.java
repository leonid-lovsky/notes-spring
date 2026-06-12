package com.example.user.service;

import com.example.user.domain.User;
import com.example.user.domain.UserOutputPort;
import com.example.user.domain.UserUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
class UserService implements UserUseCase {

    private final UserOutputPort userOutputPort;

    UserService(UserOutputPort userOutputPort) {
        this.userOutputPort = userOutputPort;
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(UUID.randomUUID(), username, email, password);
        userOutputPort.add(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userOutputPort.findById(id)
            .orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userOutputPort.findByUsername(username)
            .orElseThrow(() -> new NoSuchElementException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userOutputPort.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userOutputPort.findAll();
    }

    @Override
    public User update(UUID id, String username, String email, String password) {
        User user = userOutputPort.findById(id)
            .orElseThrow(() -> new NoSuchElementException(id.toString()));
        User updated = new User(user.id(), username, email, password);
        userOutputPort.replace(updated);
        return updated;
    }

    @Override
    public void delete(UUID id) {
        if (!userOutputPort.existsById(id)) {
            throw new NoSuchElementException(id.toString());
        }
        userOutputPort.remove(id);
    }
}
