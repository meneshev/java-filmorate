package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userDbStorage")
public class UserDBStorage extends BaseStorage<User> implements UserStorage {
    private static final String GET_USER_BY_ID_SQL = """
            SELECT
            	USER_ID,
            	USER_EMAIL,
            	USER_LOGIN,
            	USER_NAME,
            	USER_BIRTHDAY
            FROM `USER`
            WHERE USER_ID = ?
            """;

    private static final String GET_ALL_USERS_SQL = """
            SELECT
            	USER_ID,
            	USER_EMAIL,
            	USER_LOGIN,
            	USER_NAME,
            	USER_BIRTHDAY
            FROM `USER`
            ORDER BY USER_ID
            """;

    private static final String UPDATE_USER_BY_ID_SQL = """
            UPDATE PUBLIC.`USER`
            SET USER_EMAIL=?, USER_LOGIN=?, USER_NAME=?, USER_BIRTHDAY=?
            WHERE USER_ID=?
            """;

    private static final String CREATE_USER_BY_ID_SQL = """
            INSERT INTO PUBLIC.`USER`
            (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY)
            VALUES(?, ?, ?, ?)
            """;

    private static final String DELETE_USER_BY_ID_SQL = """
            DELETE FROM PUBLIC.`USER`
            WHERE USER_ID = ?
            """;

    private static final String GET_MUTUAL_FRIENDS_BY_USER_ID_SQL = """
            SELECT
            	u.USER_ID,
            	u.USER_EMAIL,
            	u.USER_LOGIN,
            	u.USER_NAME,
            	u.USER_BIRTHDAY	
            FROM FRIEND f
            INNER JOIN `USER` u ON u.USER_ID = f.ADDED_USER_ID
            WHERE f.ADDING_USER_ID = ?
            AND f.ADDED_USER_ID IN (
            	SELECT
            		f.ADDED_USER_ID
            	FROM FRIEND f
            	WHERE f.ADDING_USER_ID = ?
            )
            """;

    private static final String GET_FRIENDS_BY_USER_ID_SQL = """
            SELECT
            	u.USER_ID,
            	u.USER_EMAIL,
            	u.USER_LOGIN,
            	u.USER_NAME,
            	u.USER_BIRTHDAY	
            FROM FRIEND f
            INNER JOIN `USER` u ON u.USER_ID = f.ADDED_USER_ID
            WHERE f.ADDING_USER_ID = ?
            """;

    private static final String ADD_FRIEND_BY_USER_ID_SQL = """
            INSERT INTO PUBLIC.FRIEND (ADDING_USER_ID, ADDED_USER_ID, ISACCEPTED)
            SELECT t.* FROM (VALUES (?, ?, false))
            	AS t(ADDING_ID, ADDED_ID, ACCEPTED)
            WHERE NOT EXISTS (
                SELECT 1 FROM PUBLIC.FRIEND
                WHERE ADDING_USER_ID = t.ADDING_ID
                  AND ADDED_USER_ID = t.ADDED_ID
            )
            """;

    private static final String DELETE_FRIEND_BY_USER_ID_SQL = """
            DELETE FROM PUBLIC.FRIEND
            WHERE ADDING_USER_ID = ?
            AND ADDED_USER_ID = ?
            """;

    public UserDBStorage(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper, UserRowMapper userRowMapper) {
        super(jdbcTemplate, rowMapper, User.class);
        this.userRowMapper = userRowMapper;
    }

    private final UserRowMapper userRowMapper;

    @Override
    public User create(User user) {
        Long id = insert(CREATE_USER_BY_ID_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        user.setId(id);

        return getUserById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        update(UPDATE_USER_BY_ID_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return getUserById(user.getId()).orElse(null);
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_USER_BY_ID_SQL, id);
    }

    @Override
    public List<User> getAllUsers() {
        return findMany(GET_ALL_USERS_SQL);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return findOne(GET_USER_BY_ID_SQL, id);
    }

    @Override
    public void addFriend(Long userId, Long friendUserId) {
        jdbcTemplate.update(ADD_FRIEND_BY_USER_ID_SQL, userId, friendUserId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendUserId) {
        jdbcTemplate.update(DELETE_FRIEND_BY_USER_ID_SQL, userId, friendUserId);
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        return jdbcTemplate.query(GET_FRIENDS_BY_USER_ID_SQL, userRowMapper, userId);
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        return jdbcTemplate.query(GET_MUTUAL_FRIENDS_BY_USER_ID_SQL, userRowMapper, userId, otherUserId);
    }
}
