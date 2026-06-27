package com.example.user.domain;

import java.util.UUID;

public interface UserExistsByIdPort {

	boolean existsById(UUID id);

}
