package com.example.user.data.mongodb.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.user.data.mongodb.model.UserDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMongoRepository extends MongoRepository<UserDocument, UUID> {

    Optional<UserDocument> findByUsername(String username);

    Optional<UserDocument> findByEmail(String email);
}
