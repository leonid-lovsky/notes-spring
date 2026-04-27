package com.example;

import jakarta.validation.constraints.NotBlank;

record NoteRequest(
    @NotBlank String content // TODO
) {

}
