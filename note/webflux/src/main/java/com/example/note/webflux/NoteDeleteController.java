package com.example.note.webflux;

import java.util.UUID;

import com.example.note.contract.reactive.NoteExistsByIdContractReactive;
import com.example.note.contract.reactive.NoteRemoveContractReactive;
import com.example.note.domain.NoteNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notes")
class NoteDeleteController {

    private final NoteExistsByIdContractReactive noteExistsByIdContractReactive;

    private final NoteRemoveContractReactive noteRemoveContractReactive;

    NoteDeleteController(NoteExistsByIdContractReactive noteExistsByIdContractReactive,
            NoteRemoveContractReactive noteRemoveContractReactive) {
        this.noteExistsByIdContractReactive = noteExistsByIdContractReactive;
        this.noteRemoveContractReactive = noteRemoveContractReactive;
    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return this.noteExistsByIdContractReactive.existsById(id)
            .flatMap((exists) -> exists ? this.noteRemoveContractReactive.remove(id)
                    .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                    : Mono.error(new NoteNotFoundException(id)));
    }

}
