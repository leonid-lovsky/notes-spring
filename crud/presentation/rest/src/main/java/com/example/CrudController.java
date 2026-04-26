package com.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: @RestController
// TODO: @RequestMapping("/path")
@Validated
@RequiredArgsConstructor
class CrudController<Request, Response, ID> {

    private final CrudService<Request, Response, ID> service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Response> create(@Valid @RequestBody Request requestModel) {
        Response responseModel = service.create(requestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<Response>> read() {
        List<Response> responseModelList = service.read();
        return ResponseEntity.status(HttpStatus.OK).body(responseModelList);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> read(@Valid @PathVariable ID id) {
        Response responseModel = service.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> replace(@Valid @PathVariable ID id, @Valid @RequestBody Request requestModel) {
        Response responseModel = service.replace(id, requestModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> update(@Valid @PathVariable ID id, @Valid @RequestBody Request requestModel) {
        Response responseModel = service.update(id, requestModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Response> delete(@Valid @PathVariable ID id) {
        Response responseModel = service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}
