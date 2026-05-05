package com.example.crud.presentation.rest;

import com.example.crud.contract.CrudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
public abstract class CrudController<CreateRequest, UpdateRequest, ReplaceRequest, Response, ID> {

    private final CrudService<CreateRequest, UpdateRequest, ReplaceRequest, Response, ID> service;

    protected CrudController(CrudService<CreateRequest, UpdateRequest, ReplaceRequest, Response, ID> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Response> create(@Valid @RequestBody CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Response>> readAll() {
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> readById(@PathVariable ID id) {
        return ResponseEntity.ok(service.readById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable ID id, @Valid @RequestBody UpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> replace(@PathVariable ID id, @Valid @RequestBody ReplaceRequest request) {
        return ResponseEntity.ok(service.replace(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable ID id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
