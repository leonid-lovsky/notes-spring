package com.example.user.data.jdbc;

import com.example.user.domain.UserExistsByIdPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class UserExistsByIdPortAdapter implements UserExistsByIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserExistsByIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsById(UUID id) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE id = :id", Map.of("id", id),
                Integer.class);
        return count != null && count > 0;
    }

}
