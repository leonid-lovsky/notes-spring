package com.example;

import java.util.List;

interface NoteService {

    NoteResponseBody create(NoteRequestBody payload);

    NoteResponseBody readByIdentity(NoteIdentity identity);

    List<NoteResponseBody> readAll();

    NoteResponseBody updateById(NoteIdentity identity, NoteRequestBody payload);

    NoteResponseBody replaceById(NoteIdentity identity, NoteRequestBody payload);

    NoteResponseBody deleteById(NoteIdentity identity);
}
