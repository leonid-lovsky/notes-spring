package com.example.user.webmvc;

import java.util.List;

import com.example.user.contract.UserInterface;
import com.example.user.domain.UserResponse;

import org.springframework.http.ResponseEntity;

public interface UserControllerInterface extends
    UserInterface<ResponseEntity<Boolean>, ResponseEntity<UserResponse>, ResponseEntity<List<UserResponse>>> {

}
