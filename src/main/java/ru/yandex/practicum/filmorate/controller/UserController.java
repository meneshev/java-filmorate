package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserRequest newUser) {
        return userService.create(newUser);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UpdateUserRequest newUser) {
        return userService.update(newUser);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<UserDto> getFriends(@PathVariable Long id) {
        return userService.getFriendsByUserId(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        return userService.getUserById(id);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        return userService.getUserById(id);
    }
}
