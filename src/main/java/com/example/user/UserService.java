package com.example.user;

import com.example.user.mapping.UserMapper;
import com.example.user.persistence.UserEntity;
import com.example.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserView registerUser(RegisterUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(command.username());
        userEntity.setDisplayName(command.displayName());
        userEntity.setPasswordHash(passwordEncoder.encode(command.password()));

        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toView(savedUser);
    }

    @Transactional(readOnly = true)
    public UserView getCurrentUserProfile() {
        return userMapper.toView(getCurrentUserEntity());
    }

    private UserEntity getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required.");
        }

        return userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found."));
    }

    @Transactional(readOnly = true)
    public AuthenticatedUser getCurrentUser() {
        UserEntity userEntity = getCurrentUserEntity();
        return new AuthenticatedUser(userEntity.getId(), userEntity.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return User.withUsername(userEntity.getUsername())
            .password(userEntity.getPasswordHash())
            .authorities("ROLE_USER")
            .build();
    }
}
