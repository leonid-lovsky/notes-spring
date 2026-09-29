package com.example.note;

import java.util.UUID;
import org.springframework.data.annotation.Id;

public record Note(@Id UUID id, String content) {}
