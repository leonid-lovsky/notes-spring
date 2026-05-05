package com.example.crud.contract;

import java.util.List;

public interface CrudService<CreateRequest, UpdateRequest, ReplaceRequest, Response, ID> {

    Response create(CreateRequest request);

    List<Response> readAll();

    Response readById(ID id);

    Response update(ID id, UpdateRequest request);

    Response replace(ID id, ReplaceRequest request);

    Response delete(ID id);
}
