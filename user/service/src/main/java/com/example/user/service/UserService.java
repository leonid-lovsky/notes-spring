package com.example.user.service;

import com.example.user.domain.User;
import com.example.user.domain.UserRepository;
import com.example.user.domain.UserUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
class UserService implements UserUseCase {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(UUID.randomUUID(), username, email, password);
        userRepository.add(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NoSuchElementException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String username, String email, String password) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(id.toString()));
        User updated = new User(user.id(), username, email, password);
        userRepository.replace(updated);
        return updated;
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException(id.toString());
        }
        userRepository.remove(id);
    }
}
