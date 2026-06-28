package com.example.user.contract;

import java.util.UUID;

public interface UserExistsByIdContract {

    boolean existsById(UUID id);

}
