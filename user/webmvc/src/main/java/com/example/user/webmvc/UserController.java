package com.example.user.webmvc;

import com.example.user.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserExistsByIdPort userExistsById;
    private final UserFindByIdPort userFindById;
    private final UserFindAllPort userFindAll;
    private final UserAddPort userAdd;
    private final UserReplacePort userReplace;
    private final UserRemovePort userRemove;

    UserController(UserExistsByIdPort userExistsById, UserFindByIdPort userFindById, UserFindAllPort userFindAll,
                   UserAddPort userAdd, UserReplacePort userReplace, UserRemovePort userRemove) {
        this.userExistsById = userExistsById;
        this.userFindById = userFindById;
        this.userFindAll = userFindAll;
        this.userAdd = userAdd;
        this.userReplace = userReplace;
        this.userRemove = userRemove;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userFindAll.findAll().stream()
            .map(UserResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        User user = userFindById.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(user));
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User user = userAdd.add(new User(null, request.username(), request.email()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        if (!userExistsById.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        User updated = new User(id, request.username(), request.email());
        userReplace.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!userExistsById.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRemove.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
