package com.example.user.data.jpa;

import com.example.user.domain.UserProfile;
import com.example.user.domain.UserProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserProfileRepositoryAdapter implements UserProfileRepository {

    private final UserProfileJpaRepository jpa;

    UserProfileRepositoryAdapter(UserProfileJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public UserProfile save(UserProfile profile) {
        return toProfile(jpa.save(toEntity(profile)));
    }

    @Override
    public Optional<UserProfile> findById(UUID id) {
        return jpa.findById(id).map(this::toProfile);
    }

    @Override
    public Optional<UserProfile> findBySubject(String subject) {
        return jpa.findBySubject(subject).map(this::toProfile);
    }

    private UserProfileEntity toEntity(UserProfile p) {
        var e = new UserProfileEntity();
        e.id = p.id();
        e.subject = p.subject();
        e.username = p.username();
        e.email = p.email();
        return e;
    }

    private UserProfile toProfile(UserProfileEntity e) {
        return new UserProfile(e.id, e.subject, e.username, e.email);
    }
}
