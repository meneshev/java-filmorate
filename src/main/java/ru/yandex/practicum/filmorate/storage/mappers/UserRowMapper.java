package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Component
public class UserRowMapper implements RowMapper<User> {
    private final String GET_ACCEPTED_FRIENDS_BY_USER_ID = """
            SELECT
            	ADDED_USER_ID
            FROM FRIEND
            WHERE ADDING_USER_ID = ?
            AND ISACCEPTED = true
            """;

    private final String GET_FRIENDS_BY_USER_ID = """
            SELECT
            	ADDED_USER_ID, ISACCEPTED
            FROM FRIEND
            WHERE ADDING_USER_ID = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public UserRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("USER_EMAIL"));
        user.setLogin(rs.getString("USER_LOGIN"));
        user.setName(rs.getString("USER_NAME"));
        user.setBirthday(rs.getDate("USER_BIRTHDAY").toLocalDate());

        Map<Long, Boolean> friends = new LinkedHashMap<>();
        jdbcTemplate.query(
                GET_FRIENDS_BY_USER_ID,
                rs1 -> {
                    while (rs1.next()) {
                        friends.put(
                                rs1.getLong("ADDED_USER_ID"),
                                rs1.getBoolean("ISACCEPTED")
                        );
                    }
                },
                user.getId()
        );

        user.setFriends(friends);
        return user;
    }
}
