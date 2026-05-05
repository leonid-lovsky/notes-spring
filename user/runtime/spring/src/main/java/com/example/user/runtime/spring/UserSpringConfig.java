package com.example.user.runtime.spring;

import com.example.user.contract.UserService;
import com.example.user.contract.UserStore;
import com.example.user.persistence.jpa.UserJpaAdapter;
import com.example.user.persistence.jpa.UserJpaMapper;
import com.example.user.persistence.jpa.UserRepository;
import com.example.user.service.DefaultUserService;
import com.example.user.service.UserServiceMapper;
import com.example.user.service.UserServiceMapperImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSpringConfig {

    @Bean
    UserServiceMapper userServiceMapper() {
        return new UserServiceMapperImpl();
    }

    @Bean
    UserStore userStore(UserRepository repository, UserJpaMapper mapper) {
        return new UserJpaAdapter(repository, mapper);
    }

    @Bean
    DefaultUserService userCoreService(UserStore userStore, UserServiceMapper mapper) {
        return new DefaultUserService(userStore, mapper);
    }

    @Bean
    @Primary
    UserService userService(DefaultUserService userCoreService) {
        return new TransactionalUserService(userCoreService);
    }
}
