package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> findAll() {
        return userStorage.getAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> {
            log.error("User specified wrong path variable [userId]");
            return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
        });

        return UserMapper.mapToUserDto(user);
    }

    public UserDto create(CreateUserRequest newUser) {
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        User user = UserMapper.mapToUser(newUser);
        user = userStorage.create(user);
        log.info("new user was created: {}", newUser);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto update(UpdateUserRequest newUser) {
        if (newUser.getId() == null) {
            log.error("User not specified required parameter in request body [user.id]");
            throw new ValidationException("При изменении пользователя необходимо указание идентификатора", null);
        }
        User user = UserMapper.mapToUser(newUser);

        return UserMapper.mapToUserDto(userStorage.getUserById(newUser.getId())
                .map(existingUser -> {
                    if (newUser.getName() == null) {
                        newUser.setName(newUser.getLogin());
                    }
                    log.info("user was updated: {}", newUser);
                    return userStorage.update(user);
                })
                .orElseThrow(() -> {
                    log.error("User specified wrong parameter in request body [user.id]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", newUser.getId());
                }));
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

    public List<UserDto> getMutualFriends(Long userId, Long otherId) {
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

        return userStorage.getMutualFriends(userId, otherId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFriendsByUserId(Long id) {
        userStorage.getUserById(id)
                .orElseThrow(() -> {
                    log.error("User specified wrong path variable [id]");
                    return new ObjectNotFoundException("Указан несуществующий идентификатор пользователя", id);
                });

        return userStorage.getFriendsByUserId(id).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}
