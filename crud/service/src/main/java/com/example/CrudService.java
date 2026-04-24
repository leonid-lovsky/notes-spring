package com.example;

import java.util.List;

interface CrudService<CrudRequestModel, CrudResponseModel, CrudIdentity> {

    CrudResponseModel create(CrudRequestModel requestModel);

    List<CrudResponseModel> read();

    CrudResponseModel read(CrudIdentity identity);

    CrudResponseModel replace(CrudIdentity identity, CrudRequestModel requestModel);

    CrudResponseModel update(CrudIdentity identity, CrudRequestModel requestModel);

    CrudResponseModel delete(CrudIdentity identity);
}
