package com.example.notes_spring.controller;

import com.example.notes_spring.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotesController {

    private final NotesService service;
}
