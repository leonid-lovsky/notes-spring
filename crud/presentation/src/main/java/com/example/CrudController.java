package com.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController
// @RequestMapping("/path")
@Validated
@RequiredArgsConstructor
class CrudController<CrudRequestModel, CrudResponseModel, CrudIdentity> {

    private final CrudService<CrudRequestModel, CrudResponseModel, CrudIdentity> service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CrudResponseModel> create(@Valid @RequestBody CrudRequestModel requestModel) {
        CrudResponseModel responseModel = service.create(requestModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<CrudResponseModel>> read() {
        List<CrudResponseModel> responseModelList = service.read();
        return ResponseEntity.status(HttpStatus.OK).body(responseModelList);
    }

    @GetMapping("/{identity}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CrudResponseModel> read(@Valid @PathVariable CrudIdentity identity) {
        CrudResponseModel responseModel = service.read(identity);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PutMapping("/{identity}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CrudResponseModel> replace(@Valid @PathVariable CrudIdentity identity, @Valid @RequestBody CrudRequestModel requestModel) {
        CrudResponseModel responseModel = service.replace(identity, requestModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @PatchMapping("/{identity}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CrudResponseModel> update(@Valid @PathVariable CrudIdentity identity, @Valid @RequestBody CrudRequestModel requestModel) {
        CrudResponseModel responseModel = service.update(identity, requestModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    @DeleteMapping("/{identity}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<CrudResponseModel> delete(@Valid @PathVariable CrudIdentity identity) {
        CrudResponseModel responseModel = service.delete(identity);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}
