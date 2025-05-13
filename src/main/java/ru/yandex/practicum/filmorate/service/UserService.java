package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.getAllUsers();
    }

    public Optional<User> getUserById(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            log.error("User specified wrong path variable [userId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
        }
        return userStorage.getUserById(id);
    }

    public User create(User newUser) {
        if (newUser.getId() != null) {
            log.error("User specified wrong parameter in request body [user.id]");
            throw new ValidationException("При добавлении нового пользователя не нужно указывать идентификатор",
                    newUser.getId().toString());
        }

        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        log.info("new user was created: {}", newUser);
        return userStorage.create(newUser);
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.error("User not specified required parameter in request body [user.id]");
            throw new ValidationException("При изменении пользователя необходимо указание идентификатора", null);
        }

        if (userStorage.getAllUsers().stream().noneMatch(user -> Objects.equals(user.getId(), newUser.getId()))) {
            log.error("User specified wrong parameter in request body [user.id]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", newUser.getId());
        }

        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        log.info("user was updated: {}", newUser);
        return userStorage.update(newUser);
    }

    public User addFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id).isEmpty()) {
            log.error("User specified wrong path variable [id]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
        }
        if (userStorage.getUserById(friendId).isEmpty()) {
            log.error("User specified wrong path variable [friendId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", friendId);
        }
        userStorage.addFriend(id, friendId);
        log.info("user with id:{} become a friend with user with id:{}", id, friendId);
        return userStorage.getUserById(id).get();
    }

    public User deleteFriend(Long id, Long friendId) {
        if (userStorage.getUserById(id).isEmpty()) {
            log.error("User specified wrong path variable [id]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
        }
        if (userStorage.getUserById(friendId).isEmpty()) {
            log.error("User specified wrong path variable [friendId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", friendId);
        }

        if (!userStorage.getUserById(id).get().getFriends().contains(friendId)) {
            log.error("User with id:{} is not a friend of user with id:{}", id, friendId);
            throw new ValidationException("Данного пользователя нет в друзьях", friendId.toString());
        }

        userStorage.deleteFriend(id, friendId);
        log.info("user with id:{} deleted friend with user with id:{}", id, friendId);
        return userStorage.getUserById(id).get();
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        if (userStorage.getUserById(userId).isEmpty()) {
            log.error("User specified wrong path variable [userId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", userId);
        }

        if (userStorage.getUserById(otherId).isEmpty()) {
            log.error("User specified wrong path variable [otherId]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", otherId);
        }

        return userStorage.getMutualFriends(userId, otherId);
    }

    public List<User> getFriendsByUserId(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            log.error("User specified wrong path variable [id]");
            throw new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
        }
        return userStorage.getFriendsByUserId(id);
    }
}
