package com.example.application.crud;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CrudService<Request, Response, ID> {

    Response create(@Valid @NotNull Request request);

    List<Response> readAll();

    Response readById(@NotNull ID id);

    Response updateById(@NotNull ID id, @Valid @NotNull Request request);

    Response replaceById(@NotNull ID id, @Valid @NotNull Request request);

    Response deleteById(@NotNull ID id);
}
