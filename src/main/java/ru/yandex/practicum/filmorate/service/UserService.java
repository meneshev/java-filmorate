package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id).orElseThrow(() -> {
            log.error("User specified wrong path variable [userId]");
            return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
        });
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

        return userStorage.getUserById(newUser.getId())
                .map(existingUser -> {
                    if (newUser.getName() == null) {
                        newUser.setName(newUser.getLogin());
                    }
                    log.info("user was updated: {}", newUser);
                    return userStorage.update(newUser);
                })
                .orElseThrow(() -> {
                    log.error("User specified wrong parameter in request body [user.id]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", newUser.getId());
                });
    }

    public void addFriend(Long id, Long friendId) {
        userStorage.getUserById(id)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [id]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
                });

        userStorage.getUserById(friendId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [friendId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", friendId);
                });

        userStorage.addFriend(id, friendId);
        log.info("user with id:{} become a friend with user with id:{}", id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        userStorage.getUserById(id)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [id]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
                });

        userStorage.getUserById(friendId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [friendId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", friendId);
                });

        userStorage.deleteFriend(id, friendId);
        log.info("user with id:{} deleted friend with user with id:{}", id, friendId);
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [userId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", userId);
                });

        userStorage.getUserById(otherId)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [otherId]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", otherId);
                });

        return userStorage.getMutualFriends(userId, otherId);
    }

    public List<User> getFriendsByUserId(Long id) {
        userStorage.getUserById(id)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [id]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
                });

        return userStorage.getFriendsByUserId(id);
    }
}
