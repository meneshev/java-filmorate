package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);
    User update(User user);
    void delete(Long id);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void addFriend(Long userId, Long friendUserId);
    void deleteFriend(Long userId, Long friendUserId);
    List<User> getFriendsByUserId(Long userId);
    List<User> getMutualFriends(Long userId, Long otherUserId);
}
