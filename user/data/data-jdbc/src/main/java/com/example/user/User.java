package com.example.user;

import java.util.UUID;
import org.springframework.data.annotation.Id;

public record User(@Id UUID id, String name) {}
