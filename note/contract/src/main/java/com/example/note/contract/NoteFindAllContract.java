package com.example.note.contract;

import java.util.List;

import com.example.note.domain.NoteResponse;

public interface NoteFindAllContract {

    List<NoteResponse> findAll();

}
