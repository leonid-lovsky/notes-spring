package com.example.user;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {}
