package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Request GET /users received");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User newUser) {
        log.info("Request POST /users received, body:{}", newUser);
        if (newUser.getId() != null) {
            log.error("Request POST /users, request body has id");
            throw new ValidationException("При добавлении нового пользователя не нужно указывать id");
        }

        newUser.setId(getNextId());
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);
        log.info("Request POST /users, create new object:{}", newUser);
        return newUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Request PUT /users received, body:{}", newUser);
        if (newUser.getId() == null) {
            log.error("Request PUT /users, request body hasn't id");
            throw new ValidationException("При изменении пользователя необходимо указание id");
        }

        if (!users.containsKey(newUser.getId())) {
            log.error("Request PUT /users, id {} not found", newUser.getId());
            throw new ValidationException("Указан несуществующий id");
        }

        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);
        log.info("Request PUT /users, updated object:{}", newUser);
        return newUser;
    }

    private Long getNextId() {
        return users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L) + 1L;
    }
}
