package com.example.note.webmvc;

import java.util.UUID;

import com.example.note.contract.NoteExistsByIdContract;
import com.example.note.contract.NoteRemoveContract;
import com.example.note.domain.NoteNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteDeleteController {

    private final NoteExistsByIdContract noteExistsByIdContract;

    private final NoteRemoveContract noteRemoveContract;

    NoteDeleteController(NoteExistsByIdContract noteExistsByIdContract, NoteRemoveContract noteRemoveContract) {
        this.noteExistsByIdContract = noteExistsByIdContract;
        this.noteRemoveContract = noteRemoveContract;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!this.noteExistsByIdContract.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        this.noteRemoveContract.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
