package com.example.authentication.persistence;

import com.example.authentication.AuthenticationProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExternalAuthenticationMethodRepository extends JpaRepository<ExternalAuthenticationMethodEntity, UUID> {

    List<ExternalAuthenticationMethodEntity> findAllByUserIdOrderByLinkedAtAsc(UUID userId);

    boolean existsByUserIdAndProvider(UUID userId, AuthenticationProvider provider);

    boolean existsByProviderAndExternalUserId(AuthenticationProvider provider, String externalUserId);
}
