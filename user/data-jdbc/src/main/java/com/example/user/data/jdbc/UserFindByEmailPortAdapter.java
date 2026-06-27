package com.example.user.data.jdbc;

import com.example.user.domain.UserFindByEmailPort;
import com.example.user.domain.UserResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

	private final NamedParameterJdbcTemplate jdbc;

	private final UserJdbcMapper userJdbcMapper;

	UserFindByEmailPortAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapper userJdbcMapper) {
		this.jdbc = jdbc;
		this.userJdbcMapper = userJdbcMapper;
	}

	@Override
	public Optional<UserResponse> findByEmail(String email) {
		return jdbc
			.query("SELECT id, username, email FROM users WHERE email = :email", Map.of("email", email),
					userJdbcMapper::fromRow)
			.stream()
			.findFirst();
	}

}
