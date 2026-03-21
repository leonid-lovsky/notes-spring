package com.example.note.service;

import com.example.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service @Validated
@Transactional
@RequiredArgsConstructor
class NoteServiceImpl implements NoteService {

}
