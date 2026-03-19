package com.example.authentication;

import com.example.authentication.mapping.ExternalAuthenticationMethodMapper;
import com.example.authentication.persistence.ExternalAuthenticationMethodEntity;
import com.example.authentication.persistence.ExternalAuthenticationMethodRepository;
import com.example.user.AuthenticatedUser;
import com.example.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExternalAuthenticationService {

    private final ExternalAuthenticationMethodRepository externalAuthenticationMethodRepository;
    private final ExternalAuthenticationMethodMapper externalAuthenticationMethodMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ExternalAuthenticationMethodView> getCurrentUserMethods() {
        AuthenticatedUser currentUser = userService.getCurrentUser();
        return externalAuthenticationMethodRepository.findAllByUserIdOrderByLinkedAtAsc(currentUser.id()).stream()
            .map(externalAuthenticationMethodMapper::toView)
            .toList();
    }

    public ExternalAuthenticationMethodView linkCurrentUserMethod(LinkExternalAuthenticationCommand command) {
        AuthenticatedUser currentUser = userService.getCurrentUser();

        if (externalAuthenticationMethodRepository.existsByUserIdAndProvider(currentUser.id(), command.provider())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This authentication provider is already linked to the current user."
            );
        }

        if (externalAuthenticationMethodRepository.existsByProviderAndExternalUserId(command.provider(), command.externalUserId())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This external account is already linked to another user."
            );
        }

        ExternalAuthenticationMethodEntity externalAuthenticationMethodEntity =
            externalAuthenticationMethodMapper.commandToEntity(command);
        externalAuthenticationMethodEntity.setUserId(currentUser.id());

        ExternalAuthenticationMethodEntity savedMethod =
            externalAuthenticationMethodRepository.save(externalAuthenticationMethodEntity);

        return externalAuthenticationMethodMapper.toView(savedMethod);
    }
}
