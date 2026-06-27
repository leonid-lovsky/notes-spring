package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserFindAllPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

    private final UserJpaRepository userJpaRepository;

    UserFindAllPortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(e -> new User(e.getId(), e.getUsername(), e.getEmail()))
                .toList();
    }
}
