import com.example.application.crud.CrudService;
import com.example.application.user.UserRequest;
import com.example.application.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/user")
@RequiredArgsConstructor
class UserController {

    private final CrudService<UserRequest, UserResponse, UUID> service;

    @PostMapping
    ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = service.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> read() {
        List<UserResponse> userResponse = service.readAll();
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> readById(@PathVariable UUID id) {
        UserResponse userResponse = service.readById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserResponse> updateById(@PathVariable UUID id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = service.updateById(id, userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> replaceById(@PathVariable UUID id, @Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = service.replaceById(id, userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<UserResponse> deleteById(@PathVariable UUID id) {
        UserResponse userResponse = service.deleteById(id);
        return ResponseEntity.ok(userResponse);
    }
}
