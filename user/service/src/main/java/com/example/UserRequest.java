package com.example;

import jakarta.validation.constraints.NotBlank;

record UserRequest(
    @NotBlank String username // TODO
) {

}
