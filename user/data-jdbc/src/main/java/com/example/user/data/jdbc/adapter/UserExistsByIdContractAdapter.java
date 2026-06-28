package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.user.contract.UserExistsByIdContract;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserExistsByIdContractAdapter implements UserExistsByIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserExistsByIdContractAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsById(UUID id) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE id = :id", Map.of("id", id),
                Integer.class);
        return count != null && count > 0;
    }

}
