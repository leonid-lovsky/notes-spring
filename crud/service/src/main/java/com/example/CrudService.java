package com.example;

import java.util.List;

interface CrudService<Request, Response, ID> {

    Response create(Request request);

    List<Response> read();

    Response read(ID id);

    Response replace(ID id, Request request);

    Response update(ID id, Request request);

    Response delete(ID id);
}
