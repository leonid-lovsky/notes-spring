package com.example.note.contract;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteAddContract {

    NoteResponse add(NoteRequest request);

}
