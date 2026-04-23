package com.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
class CrudController<CrudRequestBody, CrudResponseBody, CrudId> {

    private final CrudService<CrudRequestBody, CrudResponseBody, CrudId> service;

    @PostMapping
    ResponseEntity<CrudResponseBody> create(@Valid @RequestBody CrudRequestBody requestBody) {
        CrudResponseBody responseBody = service.create(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping
    ResponseEntity<List<CrudResponseBody>> read() {
        List<CrudResponseBody> responseBodyList = service.read();
        return ResponseEntity.status(HttpStatus.OK).body(responseBodyList);
    }

    @GetMapping("/{id}")
    ResponseEntity<CrudResponseBody> read(@Valid @PathVariable CrudId id) {
        CrudResponseBody responseBody = service.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PutMapping("/{id}")
    ResponseEntity<CrudResponseBody> replace(@Valid @PathVariable CrudId id, @Valid @RequestBody CrudRequestBody requestBody) {
        CrudResponseBody responseBody = service.replace(id, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PatchMapping("/{id}")
    ResponseEntity<CrudResponseBody> update(@Valid @PathVariable CrudId id, @Valid @RequestBody CrudRequestBody requestBody) {
        CrudResponseBody responseBody = service.update(id, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<CrudResponseBody> delete(@Valid @PathVariable CrudId id) {
        CrudResponseBody responseBody = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
