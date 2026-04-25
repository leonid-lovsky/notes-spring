package com.example;

public interface CrudMapper<Req, Res, Entity> {

    Entity toEntity(Req request);

    Res toResponse(Entity entity);

    void update(Entity entity, Req request); // PATCH
}
