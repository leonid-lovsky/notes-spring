package com.example.note.contract;

import java.util.List;

import com.example.note.domain.NoteResponse;

public interface NoteServiceInterface extends NoteInterface<Boolean, NoteResponse, List<NoteResponse>> {

}
