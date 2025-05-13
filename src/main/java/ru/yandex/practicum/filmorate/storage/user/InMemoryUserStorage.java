package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User newUser) {
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User update(User newUser) {
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriend(Long userId, Long friendUserId) {
        users.get(userId).getFriends().add(friendUserId);
        users.get(friendUserId).getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendUserId) {
        users.get(userId).getFriends().remove(friendUserId);
        users.get(friendUserId).getFriends().remove(userId);
    }

    @Override
    public List<User> getFriendsByUserId(Long userId) {
        return users.get(userId).getFriends().stream()
                .map(users::get).toList();
    }

    @Override
    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        List<User> friendsOfUser = users.get(userId).getFriends().stream()
                .map(users::get).toList();

        List<User> friendsOfOtherUser = users.get(otherUserId).getFriends().stream()
                .map(users::get).toList();

        return friendsOfUser.stream()
                .filter(friendsOfOtherUser::contains)
                .toList();
    }

    private Long getNextId() {
        return users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1L;
    }
}
