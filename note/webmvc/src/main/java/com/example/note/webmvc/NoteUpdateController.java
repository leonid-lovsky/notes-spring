package com.example.note.webmvc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteExistsByIdPort;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteReplacePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Note updated = new Note(id, request.content());
        noteReplacePort.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(NoteResponse.from(updated));
    }
}
