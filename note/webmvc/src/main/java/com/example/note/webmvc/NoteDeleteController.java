package com.example.note.webmvc;

import com.example.note.domain.NoteExistsByIdPort;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRemovePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/notes")
class NoteDeleteController {

    private final NoteExistsByIdPort noteExistsByIdPort;

    private final NoteRemovePort noteRemovePort;

    NoteDeleteController(NoteExistsByIdPort noteExistsByIdPort, NoteRemovePort noteRemovePort) {
        this.noteExistsByIdPort = noteExistsByIdPort;
        this.noteRemovePort = noteRemovePort;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!noteExistsByIdPort.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRemovePort.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
