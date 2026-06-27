package com.example.user.webmvc;

import com.example.user.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserUpdateController {

	private final UserExistsByIdPort userExistsByIdPort;

	private final UserReplacePort userReplacePort;

	UserUpdateController(UserExistsByIdPort userExistsByIdPort, UserReplacePort userReplacePort) {
		this.userExistsByIdPort = userExistsByIdPort;
		this.userReplacePort = userReplacePort;
	}

	@PutMapping("/{id}")
	ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
		if (!userExistsByIdPort.existsById(id)) {
			throw new UserNotFoundException(id);
		}
		UserResponse updated = userReplacePort.replace(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

}
