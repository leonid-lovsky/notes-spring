package com.example.user.data.jdbc;

import com.example.user.domain.UserFindByUsernamePort;
import com.example.user.domain.UserResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
class UserFindByUsernamePortAdapter implements UserFindByUsernamePort {

	private final NamedParameterJdbcTemplate jdbc;

	private final UserJdbcMapper userJdbcMapper;

	UserFindByUsernamePortAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapper userJdbcMapper) {
		this.jdbc = jdbc;
		this.userJdbcMapper = userJdbcMapper;
	}

	@Override
	public Optional<UserResponse> findByUsername(String username) {
		return jdbc
			.query("SELECT id, username, email FROM users WHERE username = :username", Map.of("username", username),
					userJdbcMapper::fromRow)
			.stream()
			.findFirst();
	}

}
