package com.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
abstract class CrudController<Request, Response, ID> {

    private final CrudService<Request, Response, ID> service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Response> create(@Valid @RequestBody Request request) {
        Response response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<Response>> read() {
        List<Response> response = service.read();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> read(@Valid @PathVariable ID id) {
        Response response = service.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> update(@Valid @PathVariable ID id, @Valid @RequestBody Request request) {
        Response response = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> replace(@Valid @PathVariable ID id, @Valid @RequestBody Request request) {
        Response response = service.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> delete(@Valid @PathVariable ID id) {
        Response response = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
