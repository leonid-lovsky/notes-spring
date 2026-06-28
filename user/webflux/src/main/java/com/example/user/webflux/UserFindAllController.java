package com.example.user.webflux;

import com.example.user.contract.reactive.UserFindAllContractReactive;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserFindAllController {

    private final UserFindAllContractReactive userFindAllContractReactive;

    UserFindAllController(UserFindAllContractReactive userFindAllContractReactive) {
        this.userFindAllContractReactive = userFindAllContractReactive;
    }

    @GetMapping
    Flux<UserResponse> findAll() {
        return this.userFindAllContractReactive.findAll();
    }

}
