package com.example.auth.runtime.spring;

import com.example.auth.contract.AuthService;
import com.example.auth.contract.AuthStore;
import com.example.auth.persistence.jpa.AuthJpaAdapter;
import com.example.auth.persistence.jpa.AuthJpaMapper;
import com.example.auth.persistence.jpa.AuthRepository;
import com.example.auth.service.AuthServiceMapper;
import com.example.auth.service.AuthServiceMapperImpl;
import com.example.auth.service.DefaultAuthService;
import com.example.user.contract.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AuthSpringConfig {

    @Bean
    AuthServiceMapper authServiceMapper() {
        return new AuthServiceMapperImpl();
    }

    @Bean
    AuthStore authStore(AuthRepository repository, AuthJpaMapper mapper) {
        return new AuthJpaAdapter(repository, mapper);
    }

    @Bean
    DefaultAuthService authCoreService(AuthStore authStore, UserService userService, AuthServiceMapper mapper) {
        return new DefaultAuthService(authStore, userService, mapper);
    }

    @Bean
    @Primary
    AuthService authService(DefaultAuthService authCoreService) {
        return new TransactionalAuthService(authCoreService);
    }
}
