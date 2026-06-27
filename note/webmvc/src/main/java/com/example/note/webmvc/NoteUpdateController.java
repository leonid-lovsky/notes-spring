package com.example.note.webmvc;

import com.example.note.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteUpdateController {

    private final NoteExistsByIdPort noteExistsByIdPort;

    private final NoteReplacePort noteReplacePort;

    NoteUpdateController(NoteExistsByIdPort noteExistsByIdPort, NoteReplacePort noteReplacePort) {
        this.noteExistsByIdPort = noteExistsByIdPort;
        this.noteReplacePort = noteReplacePort;
    }

    @PutMapping("/{id}")
    ResponseEntity<NoteResponse> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        if (!noteExistsByIdPort.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        NoteResponse updated = noteReplacePort.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

}
