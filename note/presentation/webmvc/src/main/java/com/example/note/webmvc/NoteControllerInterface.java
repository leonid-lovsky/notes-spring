package com.example.note.webmvc;

import java.util.List;

import com.example.note.contract.NoteInterface;
import com.example.note.domain.NoteResponse;

import org.springframework.http.ResponseEntity;

public interface NoteControllerInterface extends
    NoteInterface<ResponseEntity<Boolean>, ResponseEntity<NoteResponse>, ResponseEntity<List<NoteResponse>>> {

}
