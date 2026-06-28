package com.example.note.webmvc;

import java.util.UUID;

import com.example.note.contract.NoteExistsByIdContract;
import com.example.note.contract.NoteReplaceContract;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteUpdateController {

    private final NoteExistsByIdContract noteExistsByIdContract;

    private final NoteReplaceContract noteReplaceContract;

    NoteUpdateController(NoteExistsByIdContract noteExistsByIdContract, NoteReplaceContract noteReplaceContract) {
        this.noteExistsByIdContract = noteExistsByIdContract;
        this.noteReplaceContract = noteReplaceContract;
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        if (!this.noteExistsByIdContract.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        NoteResponse updated = this.noteReplaceContract.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

}
